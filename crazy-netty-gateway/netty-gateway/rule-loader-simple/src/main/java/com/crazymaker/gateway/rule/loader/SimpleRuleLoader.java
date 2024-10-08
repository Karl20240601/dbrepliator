package com.crazymaker.gateway.rule.loader;

import com.alibaba.fastjson.JSON;
import com.crazymaker.gateway.core.api.context.GatewayRequest;
import com.crazymaker.gateway.core.api.error.ResponseException;
import com.crazymaker.gateway.core.api.metadata.Rule;
import com.crazymaker.gateway.core.api.metadata.RuleLoader;
import com.crazymaker.micro.core.annotation.DefaultImplementor;
import com.crazymaker.springcloud.common.util.IOUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.crazymaker.gateway.core.api.config.GlobalSingletons.ANT_PATH_MATCHER;
import static com.crazymaker.gateway.core.api.constant.BasicConst.PREFIX_PATH_MATCHER;
import static com.crazymaker.gateway.core.api.constant.ResponseCode.PATH_NO_MATCHED;

/**
 * 简单的路由规则加载器
 *
 * 从文件加载
 */

@DefaultImplementor
@Slf4j
public class SimpleRuleLoader implements RuleLoader {
    ConcurrentHashMap<String, List<Rule>> serviceRuleMap = new ConcurrentHashMap<>();

    //	规则集合
    private ConcurrentHashMap<String /* ruleId */ , Rule> ruleMap = new ConcurrentHashMap<>();

    // 精准 路径 规则集合
    private ConcurrentHashMap<String /* 路径 */ , Rule> prefixRuleMap = new ConcurrentHashMap<>();

    //matcher 规则
    private ConcurrentHashMap<String /* matcher */ , Rule> matcherRuleMap = new ConcurrentHashMap<>();

    public SimpleRuleLoader() {
    }


    @Override
    public void putRule(String ruleId, Rule rule) {
        ruleMap.put(ruleId, rule);
    }

    @Override
    public void loadAllRule(List<Rule> ruleList) {


        for (Rule rule : ruleList) {

            if (rule.getMatchType().equals(PREFIX_PATH_MATCHER)) {
                //前缀匹配
                //类似精准匹配
                prefixRuleMap.put(rule.getPrefix(), rule);
            } else {
                //ant 风格匹配
                matcherRuleMap.put(rule.getPrefix(), rule);
            }

            ruleMap.put(rule.getId(), rule);

            //如果不存在，就设置一个 空的 ArrayList
            List<Rule> rules = serviceRuleMap.computeIfAbsent(rule.getServiceId(), (key) -> {
                return new ArrayList<>();
            });

            rules.add(rule);

        }
    }

    @Override
    public Rule getRule(String ruleId) {
        return ruleMap.get(ruleId);
    }

    /**
     * @一句话介绍： 通过url 进行 定义匹配
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @详细介绍： 1.精准匹配
     * 2. ant 匹配
     */
    @Override
    public Rule findRule(GatewayRequest rapidRequest) {


        // step 1: 精准匹配
        // step 2: ant 匹配

        String uri = rapidRequest.getUri();

        Rule rule = prefixRuleMap.get(uri);
        if (null != rule) {
            // step 1: 精准匹配
            return rule;
        }

        Iterator<String> patterns = matcherRuleMap.keySet().iterator();
        while (patterns.hasNext()) {
            String pattern = patterns.next();

            // step 2: ant 匹配

            if (!ANT_PATH_MATCHER.match(pattern, uri)) {
                return matcherRuleMap.get(pattern);
            }
        }

        //异常
        throw new ResponseException(PATH_NO_MATCHED);

    }

    @Override
    public void removeRule(String ruleId) {
        ruleMap.remove(ruleId);
    }

    @Override
    public ConcurrentHashMap<String, Rule> getRuleMap() {
        return ruleMap;
    }

    @Override
    public Rule getRuleByPath(String path) {
        return prefixRuleMap.get(path);
    }


    // 来自于微内核
    @Override
    public String getType() {
        return null;
    }

    @Override
    public void prepare(Object params) throws Throwable {

    }

    @Override
    public void boot() throws Throwable {
        log.info("micro core is booted:" + SimpleRuleLoader.class);

        String json = IOUtil.loadJarFile(SimpleRuleLoader.class.getClassLoader(), "rule.json");
        List<Rule> rules = JSON.parseObject(json).getJSONArray("rules").toJavaList(Rule.class);

        loadAllRule(rules);
    }

    @Override
    public void onComplete() throws Throwable {

    }

    @Override
    public void shutdown() throws Throwable {

    }
}
