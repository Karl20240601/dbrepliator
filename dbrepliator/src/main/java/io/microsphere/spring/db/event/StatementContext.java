package io.microsphere.spring.db.event;

public class StatementContext {
    private StringBuilder stringBuilder = new StringBuilder();
    private int sqlNum;

    public StatementContext() {

    }

    public StatementContext(String sql) {
        appendSql(sql);
    }


    private void appendSql(String sql) {
        if (sqlNum == 0) {
            stringBuilder.append(sql);
            return;
        }
        stringBuilder.append(";");
        stringBuilder.append(sql);
        increSqlNum();
    }

    private void increSqlNum() {
        sqlNum++;
    }


    public boolean isBatchUpdate() {
        return this.sqlNum > 1;
    }

    public String getSql() {
        return stringBuilder.length() <= 0 ? null : stringBuilder.toString();
    }
}
