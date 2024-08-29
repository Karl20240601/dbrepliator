package io.microsphere.spring.db.support.event;

import io.microsphere.spring.db.support.wrapper.SqlSessionContext;
import org.springframework.context.ApplicationEventPublisher;

public class SqlSessionEventLlistenerImpl implements SqlSessionEventLlistener {
    private ApplicationEventPublisher applicationEventPublisher;

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

    @Override
    public void onRollback(SqlSessionContext sqlSessionContext) {
        publishEvent(sqlSessionContext);
    }

    private DbDataUpdateEvent createDbDataUpdateEvent(SqlSessionContext sqlSessionContext) {
        DbDataUpdateEvent dbDataUpdateEvent = new DbDataUpdateEvent(this);
        dbDataUpdateEvent.setSqlSessionContext(sqlSessionContext);
        return dbDataUpdateEvent;
    }
}
