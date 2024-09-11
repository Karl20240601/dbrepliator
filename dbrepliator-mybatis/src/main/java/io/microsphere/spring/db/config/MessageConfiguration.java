package io.microsphere.spring.db.config;

import io.microsphere.spring.common.binds.MessageTargetRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.MessageChannel;


public class MessageConfiguration {

    public final static String BEAN_NAME = "dbReplicatorMessageConfiguration";


    private MessageTargetRegistry messageTargetRegistry;


    public void init(ApplicationContext context, DbReplConfiguration dbReplicatorConfiguration) {
        this.messageTargetRegistry = context.getBean(MessageTargetRegistry.class);
    }


    public MessageChannel getMessageChannel(String domain) {
        return messageTargetRegistry.getProducerMessageChannel(domain);
    }


}
