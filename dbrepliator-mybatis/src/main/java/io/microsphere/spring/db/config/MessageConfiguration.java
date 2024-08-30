package io.microsphere.spring.db.config;

import io.microsphere.spring.common.producer.MessageChannelFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.messaging.MessageChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageConfiguration implements SmartApplicationListener {
    private ApplicationContext context;
    private Map<String, MessageChannel> messageChannelDomianMap = new HashMap<>();

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return ContextRefreshedEvent.class.equals(eventType);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            onContextRefreshedEvent((ContextRefreshedEvent) event);
        }
    }

    private void onContextRefreshedEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        DBReplicatorConfiguration dbReplicatorConfiguration = context.getBean(DBReplicatorConfiguration.class);
        dbReplicatorConfiguration.init();
        initMessageChannel(context, dbReplicatorConfiguration);
    }

    private void initMessageChannel(ApplicationContext context, DBReplicatorConfiguration dbReplicatorConfiguration) {
        MessageChannelFactory bean = context.getBean(MessageChannelFactory.class);

        List<String> domains = dbReplicatorConfiguration.getDomains();
        for (String domain : domains) {
            MessageChannel messageChannel = bean.createMessageChannel(domain);
            messageChannelDomianMap.put(domain, messageChannel);
        }
    }

    public MessageChannel getMessageChannel(String domain) {
        return messageChannelDomianMap.get(domain);
    }


}
