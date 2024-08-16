package io.microsphere.spring.db.utils;

import org.apache.commons.lang3.StringUtils;

public class SqlStringUtils {
    public final static char PLACEHOLDERS_CHAR = '?';

    /**
     * 统计sql语句的占位符
     *
     * @return
     */
    public static final int findNumberOfPlaceholders(String sql) {
        int numbers = 0;
        if (StringUtils.isBlank(sql)) {
            return 0;
        }
        int length = sql.length();

        for (int i = 0; i < length; i++) {
            if (PLACEHOLDERS_CHAR == sql.charAt(i)) {
                numbers++;
            }
        }
        return numbers;
    }

    public static final String findTableNameBysql(String sql){
        return "";
    }
}
