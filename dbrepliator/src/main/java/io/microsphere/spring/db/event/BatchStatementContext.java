package io.microsphere.spring.db.event;

import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

public class BatchStatementContext {
    private List<String> sqlList;

    public BatchStatementContext() {

    }
    public void addSql(String sql) {
        if (CollectionUtils.isEmpty(sqlList)) {
            sqlList = new LinkedList<>();
        }
        sqlList.add(sql);
    }

}
