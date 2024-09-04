package io.microsphere.spring.db.support.wrapper;


import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SqlSessionContext implements Serializable {
    private static final long serialVersionUID = 5436603508567014260L;
    private  List<StatementParamer> statementParamers = new ArrayList<>();
    private boolean autoCommit;
    private String sqlSessionFactorybeanName;
    private Object [] sqlSessionContructvalues;

    public SqlSessionContext() {
    }

    public void addStatementParamer(StatementParamer statementParamer) {
        statementParamers.add(statementParamer);
    }

    public List<StatementParamer> getStatementParamers() {
        return statementParamers;
    }

    public void setStatementParamers(List<StatementParamer> statementParamers) {
        if(CollectionUtils.isEmpty(statementParamers)){
            return;
        }
        this.statementParamers.addAll(statementParamers);
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public String getSqlSessionFactorybeanName() {
        return sqlSessionFactorybeanName;
    }

    public void setSqlSessionFactorybeanName(String sqlSessionFactorybeanName) {
        this.sqlSessionFactorybeanName = sqlSessionFactorybeanName;
    }



    public Object[] getSqlSessionContructvalues() {
        return sqlSessionContructvalues;
    }

    public void setSqlSessionContructvalues(Object[] sqlSessionContructvalues) {
        this.sqlSessionContructvalues = sqlSessionContructvalues;
    }
}
