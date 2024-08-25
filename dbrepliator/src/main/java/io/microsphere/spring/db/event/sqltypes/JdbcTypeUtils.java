package io.microsphere.spring.db.event.sqltypes;


import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.time.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class JdbcTypeUtils {

    private  static Map<Class<?>, JDBCType> DEFAULT_MYSQL_TYPES = new HashMap<>();
    static {
        DEFAULT_MYSQL_TYPES.put(BigDecimal.class, JDBCType.DECIMAL);
        DEFAULT_MYSQL_TYPES.put(BigInteger.class, JDBCType.BIGINT);
        DEFAULT_MYSQL_TYPES.put(Blob.class, JDBCType.BLOB);
        DEFAULT_MYSQL_TYPES.put(Boolean.class, JDBCType.BOOLEAN);
        DEFAULT_MYSQL_TYPES.put(Byte.class, JDBCType.TINYINT);
        DEFAULT_MYSQL_TYPES.put(byte[].class, JDBCType.BINARY);
        DEFAULT_MYSQL_TYPES.put(Calendar.class, JDBCType.TIMESTAMP);
        DEFAULT_MYSQL_TYPES.put(Clob.class, JDBCType.LONGNVARCHAR);
        DEFAULT_MYSQL_TYPES.put(NClob.class, JDBCType.LONGNVARCHAR);
        DEFAULT_MYSQL_TYPES.put(Date.class, JDBCType.DATE);
        DEFAULT_MYSQL_TYPES.put(java.util.Date.class, JDBCType.TIMESTAMP);
        DEFAULT_MYSQL_TYPES.put(Double.class, JDBCType.DOUBLE);
        DEFAULT_MYSQL_TYPES.put(Duration.class, JDBCType.TIME);
        DEFAULT_MYSQL_TYPES.put(Float.class, JDBCType.FLOAT);
        DEFAULT_MYSQL_TYPES.put(InputStream.class, JDBCType.BLOB);
        DEFAULT_MYSQL_TYPES.put(Instant.class, JDBCType.TIMESTAMP);
        DEFAULT_MYSQL_TYPES.put(Integer.class, JDBCType.INTEGER);
        DEFAULT_MYSQL_TYPES.put(LocalDate.class, JDBCType.DATE);
        DEFAULT_MYSQL_TYPES.put(LocalDateTime.class, JDBCType.TIME); // default JDBC mapping is TIMESTAMP, see B-4
        DEFAULT_MYSQL_TYPES.put(LocalTime.class, JDBCType.TIME);
        DEFAULT_MYSQL_TYPES.put(Long.class, JDBCType.BIGINT);
        DEFAULT_MYSQL_TYPES.put(OffsetDateTime.class, JDBCType.TIMESTAMP); // default JDBC mapping is TIMESTAMP_WITH_TIMEZONE, see B-4
        DEFAULT_MYSQL_TYPES.put(OffsetTime.class, JDBCType.TIME); // default JDBC mapping is TIME_WITH_TIMEZONE, see B-4
        DEFAULT_MYSQL_TYPES.put(Reader.class, JDBCType.LONGNVARCHAR);
        DEFAULT_MYSQL_TYPES.put(Short.class, JDBCType.SMALLINT);
        DEFAULT_MYSQL_TYPES.put(String.class, JDBCType.VARCHAR);
        DEFAULT_MYSQL_TYPES.put(Time.class, JDBCType.TIME);
        DEFAULT_MYSQL_TYPES.put(Timestamp.class, JDBCType.TIMESTAMP);
        DEFAULT_MYSQL_TYPES.put(ZonedDateTime.class, JDBCType.TIMESTAMP); // no JDBC mapping is defined
    }

    public static JDBCType getJDBCType(Class clzz){
        JDBCType jdbcType = DEFAULT_MYSQL_TYPES.get(clzz);
        return jdbcType;
    }
    public static JDBCType getJDBCType(int typeNumber){
        JDBCType jdbcType = JDBCType.valueOf(typeNumber);
        return jdbcType;
    }
}
