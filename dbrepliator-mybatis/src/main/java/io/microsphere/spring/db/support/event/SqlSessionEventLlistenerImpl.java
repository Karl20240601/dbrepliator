package io.microsphere.spring.db.support.event;

import io.microsphere.spring.db.support.wrapper.SqlSessionContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

public class SqlSessionEventLlistenerImpl implements SqlSessionEventLlistener, ApplicationEventPublisherAware {
    private ApplicationEventPublisher applicationEventPublisher;
    public final static String BEAN_NAME = "sqlSessionEventLlistenerImpl";


    @Override
    public void onUpdate(SqlSessionContext sqlSessionContext) {
        publishEvent(sqlSessionContext);
    }

    private void publishEvent(SqlSessionContext sqlSessionContext) {
        DbDataUpdateEvent dbDataUpdateEvent = createDbDataUpdateEvent(sqlSessionContext);
        applicationEventPublisher.publishEvent(dbDataUpdateEvent);
    }

    @Override
    public void onCommit(SqlSessionContext sqlSessionContext) {
        publishEvent(sqlSessionContext);
    }


    private DbDataUpdateEvent createDbDataUpdateEvent(SqlSessionContext sqlSessionContext) {
        DbDataUpdateEvent dbDataUpdateEvent = new DbDataUpdateEvent(sqlSessionContext);
        dbDataUpdateEvent.setMessageKey(Long.toString(dbDataUpdateEvent.getTimestamp()));
        dbDataUpdateEvent.setSqlSessionContext(sqlSessionContext);
        return dbDataUpdateEvent;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
