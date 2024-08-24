package io.microsphere.spring.db.event.sqlmetadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractSqlMetaData<T> implements SqlMetaData<T> {
    private List<T> batchArgs;

    public void addBatch(T batch) {
        if (this.batchArgs == null) {
            this.batchArgs = new ArrayList<>();
        }
        batchArgs.add(batch);
    }

    public List<T> getBatchArgs() {
        return batchArgs == null ? null : Collections.unmodifiableList(batchArgs);
    }

    public void clearBatchedArgs() {
        batchArgs.clear();
    }
}
