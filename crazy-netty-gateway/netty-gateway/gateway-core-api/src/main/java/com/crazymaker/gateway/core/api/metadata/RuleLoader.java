package com.crazymaker.gateway.core.api.metadata;

import com.crazymaker.gateway.core.api.context.GatewayRequest;
import com.crazymaker.micro.core.servcie.MicroCore;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface RuleLoader extends MicroCore {


    void putRule(String ruleId, Rule rule);

    void loadAllRule(List<Rule> ruleList);

    Rule getRule(String ruleId);

    Rule findRule(GatewayRequest rapidRequest);

    void removeRule(String ruleId);

    ConcurrentHashMap<String, Rule> getRuleMap();

    Rule getRuleByPath(String path);


}
