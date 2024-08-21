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
    private Map<String, MessageChannel> subscribableChannelDomianMap = new HashMap<>();
    private DBReplicatorConfiguration dbReplicatorConfiguration;

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
        MessageChannelFactory bean = context.getBean(MessageChannelFactory.class);
        this.dbReplicatorConfiguration = context.getBean(DBReplicatorConfiguration.class);
        List<String> domains = this.dbReplicatorConfiguration.getDomains();
        for (String domain : domains) {
            MessageChannel messageChannel = bean.createMessageChannel(domain);
            subscribableChannelDomianMap.put(domain, messageChannel);
        }
    }

    public MessageChannel getMessageChannel(String domain){
        return subscribableChannelDomianMap.get(domain);
    }


}
