package io.microsphere.spring.db.event.sqlmetadata;

import java.io.Serializable;
import java.util.List;


public class StatementSqlMetaData extends AbstractSqlMetaData<String> implements Serializable {
    private static final long serialVersionUID = -8969650142533082866L;

    public String takeSingleExecuteSql() {
        return getBatchArgs().get(0);
    }

    public List<String> takeBatchleExecuteSql() {
        return getBatchArgs();
    }
}
