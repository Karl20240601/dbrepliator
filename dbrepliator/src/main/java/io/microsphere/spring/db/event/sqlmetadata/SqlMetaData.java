package io.microsphere.spring.db.event.sqlmetadata;

import java.util.List;

public interface SqlMetaData<T> {
    void addBatch(T batch);
    List<T> getBatchArgs();
    void clearBatchedArgs();
}
