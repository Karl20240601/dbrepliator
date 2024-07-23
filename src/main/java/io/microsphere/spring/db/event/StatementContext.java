package io.microsphere.spring.db.event;

public class StatementContext {
    private  String sql;
    private int menthodIndex;
    public StatementContext() {

    }

    public StatementContext(String sql) {
        this.sql = sql;
        this.menthodIndex = menthodIndex;
    }


    public String getSql() {
        return sql;
    }

    public int getMenthodIndex() {
        return menthodIndex;
    }

    public void setMenthodIndex(int menthodIndex) {
        this.menthodIndex = menthodIndex;
    }
}
