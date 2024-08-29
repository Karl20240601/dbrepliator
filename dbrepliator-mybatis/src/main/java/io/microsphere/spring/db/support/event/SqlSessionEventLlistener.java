package io.microsphere.spring.db.support.event;

import io.microsphere.spring.db.support.wrapper.SqlSessionContext;

public interface SqlSessionEventLlistener {
    void onUpdate(SqlSessionContext sqlSessionContext);

    void onCommit(SqlSessionContext sqlSessionContext);

    void onRollback(SqlSessionContext sqlSessionContext);
}
