package io.microsphere.spring.db.support.wrapper;

public class StatementParamerFactory {
    public static StatementParamer create(String statement) {
        return new StatementParamer(statement,null);
    }

    public static StatementParamer create(String statement,Object paramter) {
        return new StatementParamer(statement,paramter);
    }
}
