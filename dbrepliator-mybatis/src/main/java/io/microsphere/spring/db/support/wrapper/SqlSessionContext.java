package io.microsphere.spring.db.support.wrapper;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SqlSessionContext implements Serializable {
    private List<StatementParamer> tatementParamers;
    private boolean autoCommit;
    private String sqlSessionFactorybeanName;
    private Object [] sqlSessionContructvalues;

    public SqlSessionContext(List<StatementParamer> tatementParamers, boolean autoCommit, String sqlSessionFactorybeanName) {
        this.tatementParamers = tatementParamers;
        this.autoCommit = autoCommit;
        this.sqlSessionFactorybeanName = sqlSessionFactorybeanName;
    }

    public SqlSessionContext() {
    }

    public void addStatementParamer(StatementParamer statementParamer) {
        if (tatementParamers == null) {
            tatementParamers = new ArrayList<>();
        }
        tatementParamers.add(statementParamer);
    }


    public List<StatementParamer> getTatementParamers() {
        return tatementParamers;
    }

    public void setTatementParamers(List<StatementParamer> tatementParamers) {
        this.tatementParamers = tatementParamers;
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
