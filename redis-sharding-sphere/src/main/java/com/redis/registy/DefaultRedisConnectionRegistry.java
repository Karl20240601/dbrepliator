package com.redis.registy;

import com.redis.RedisMethodContext;
import com.redis.TagRedisConnectionNode;
import com.redis.datasource.RedisConnectionFactoryProvider;
import com.redis.datasource.config.RedisHostConfigProperties;
import com.redis.router.RouterChain;
import com.redis.router.Selector;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

public class DefaultRedisConnectionRegistry implements RedisConnectionRegistry, InitializingBean, BeanFactoryAware {

    private final RedisConnectionFactoryProvider redisConnectionFactoryProvider;
    private final RedisProperties redisProperties;
    private final RedisHostConfigProperties redisHostConfigProperties;
    private DefaultListableBeanFactory beanFactory;

    private List<TagRedisConnectionNode> tagRedisConnectionNodeList;
    private Selector selectors;
    private RouterChain routerChain;


    public DefaultRedisConnectionRegistry(RedisProperties redisProperties, RedisHostConfigProperties redisHostConfigProperties, RedisConnectionFactoryProvider redisConnectionFactoryProvider) {
        this.redisConnectionFactoryProvider = redisConnectionFactoryProvider;
        this.redisProperties = redisProperties;
        this.redisHostConfigProperties = redisHostConfigProperties;
    }

    @Override
    public TagRedisConnectionNode select(RedisMethodContext redisMethodContext) {
        List<TagRedisConnectionNode> route = this.routerChain.route(tagRedisConnectionNodeList, redisMethodContext);
        if (CollectionUtils.isEmpty(route)) {
            return null;
        }
        return selectors.select(route, redisMethodContext);
    }


    @Override
    public void register(TagRedisConnectionNode tagRedisConnectionNode) {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<TagRedisConnectionNode> tagRedisConnectionNode = redisConnectionFactoryProvider.createTagRedisConnectionNode(redisHostConfigProperties, redisProperties);
        if (CollectionUtils.isEmpty(tagRedisConnectionNode)) {
            throw new RuntimeException("没有找到相关的TagRedisConnectionNode");
        }
        this.tagRedisConnectionNodeList = Collections.unmodifiableList(tagRedisConnectionNode);

//        String[] beanNamesForTypes = this.beanFactory.getBeanNamesForType(Selector.class);
//        if (beanNamesForTypes == null || beanNamesForTypes.length <= 0) {
//            throw new RuntimeException("没有找到相关的Selector");
//        }
//        List<Selector> selectors = new ArrayList<>(beanNamesForTypes.length);
//        for (String beanName : beanNamesForTypes) {
//            selectors.add(this.beanFactory.getBean(beanName, Selector.class));
//        }
        this.selectors = this.beanFactory.getBean(Selector.class);
        this.routerChain = this.beanFactory.getBean(RouterChain.class);

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;

    }

}
