package io.microsphere.spring.db.support.wrapper;

import java.io.Serializable;

public class StatementParamer implements Serializable {
    private static final long serialVersionUID = 1772322237462180128L;
    private String statement;
    private Object objectParamter;

    public StatementParamer(String statement, Object objectParamter) {
        this.statement = statement;
        this.objectParamter = objectParamter;
    }

    public StatementParamer() {
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public Object getObjectParamter() {
        return objectParamter;
    }

    public void setObjectParamter(Object objectParamter) {
        this.objectParamter = objectParamter;
    }
}
