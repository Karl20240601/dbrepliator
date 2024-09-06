package io.microsphere.spring.db.config;

import io.microsphere.spring.common.MessagePropertysConfiguration;
import io.microsphere.spring.common.producer.MessageChannelFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.messaging.MessageChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageConfiguration {

    public final static String BEAN_NAME = "dbReplicatorMessageConfiguration";


    public final static String DB_REPLICATOR_MESSAGE_PREFIX = "db.replicator.message";
    public final static String DB_REPLICATOR_TOPICPREFIX = DB_REPLICATOR_MESSAGE_PREFIX + "topic-prefix";
    public final static String DB_REPLICATOR_TOPICPREFIX_DEFAULT = "db-synchronize-message-topic";

    public final static String MESSAGEHANDLER_NAME_PREFIX = "messageHandler";
    public final static String MESSAGEREPLSUBSCRIBABLECHANNEL = "messageReplSubscribableChannel";


    private Map<String, MessageChannel> messageChannelDomianMap = new HashMap<>();

    public void init(ApplicationContext context, DbReplConfiguration dbReplicatorConfiguration) {
        initMessageChannel(context, dbReplicatorConfiguration);
    }

    private void initMessageChannel(ApplicationContext context, DbReplConfiguration dbReplicatorConfiguration) {
        Environment environment = context.getEnvironment();
        Boolean producerEnable = environment.getProperty(MessagePropertysConfiguration.PRODUCER_PROPERTY_ENABLE, boolean.class, false);
        if (!producerEnable) {
            return;
        }
        MessageChannelFactory bean = context.getBean(MessageChannelFactory.class);
        List<String> domains = dbReplicatorConfiguration.getDomains();
        for (String domain : domains) {
            MessageChannel messageChannel = bean.createMessageChannel(createTopic(context.getEnvironment(), domain));
            messageChannelDomianMap.put(domain, messageChannel);
        }
    }

    public MessageChannel getMessageChannel(String domain) {
        return messageChannelDomianMap.get(domain);
    }


    public static String createTopic(Environment environment, String domain) {
        String property = environment.getProperty(DB_REPLICATOR_TOPICPREFIX, DB_REPLICATOR_TOPICPREFIX_DEFAULT);
        return property + domain;
    }


}
