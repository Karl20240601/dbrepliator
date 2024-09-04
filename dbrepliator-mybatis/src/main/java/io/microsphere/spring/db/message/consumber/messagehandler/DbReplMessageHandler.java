package io.microsphere.spring.db.message.consumber.messagehandler;

import com.alibaba.fastjson.JSONObject;
import io.microsphere.spring.common.comsumber.ReplMessageHandler;
import io.microsphere.spring.db.config.DBReplicatorConfiguration;
import io.microsphere.spring.db.serialize.api.ObjectInput;
import io.microsphere.spring.db.support.event.DbDataUpdateEvent;
import io.microsphere.spring.db.support.wrapper.SqlSessionContext;
import io.microsphere.spring.db.support.wrapper.StatementParamer;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.util.ReflectionUtils;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static java.util.Collections.unmodifiableMap;

public class DbReplMessageHandler implements ReplMessageHandler, ApplicationContextAware, SmartInitializingSingleton {
    private final String inputChannel;
    private ApplicationContext applicationContext;
    private DBReplicatorConfiguration dbReplicatorConfiguration;
    private static final Logger logger = LoggerFactory.getLogger(DbReplMessageHandler.class);
    private final Map<SqlSessionMethodKey, BiFunction<SqlSessionFactory, Object[], SqlSession>> redisCommandBindings = initSqlSessionBindings();



    public DbReplMessageHandler(String inputChannel) {
        this.inputChannel = inputChannel;
    }

    @Override
    public String getInputChannel() {
        return inputChannel;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        logger.debug("receive data {}", JSONObject.toJSONString(message));
        byte[] bytes = (byte[]) message.getPayload();
        try {
            processDbupdateEvent(bytes);
        } catch (Exception e) {
            logger.error("exception",e);
            throw new MessagingException(message, e);
        }
    }



    private void processDbupdateEvent(byte[] bytes) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInput deserialize = dbReplicatorConfiguration.getSerialization().deserialize(byteArrayInputStream);

        DbDataUpdateEvent dbDataUpdateEvent = deserialize.readObject(DbDataUpdateEvent.class);
        logger.debug("receive data {}", JSONObject.toJSONString(dbDataUpdateEvent));
        SqlSessionContext sqlSessionContext = dbDataUpdateEvent.getSqlSessionContext();

        SqlSession sqlSession = getSqlSession(sqlSessionContext);
        List<StatementParamer> statementParamers = dbDataUpdateEvent.getSqlSessionContext().getStatementParamers();
        for (StatementParamer statementParamer : statementParamers) {
            sqlSession.update(statementParamer.getStatement(), statementParamer.getObjectParamter());
        }
        if (!sqlSessionContext.isAutoCommit()) {
            sqlSession.commit();
        }
        sqlSession.close();
    }

    private SqlSession getSqlSession(SqlSessionContext sqlSessionContext) {
        SqlSessionFactory sqlSessionFactory = dbReplicatorConfiguration.getSqlSessionFactory(sqlSessionContext.getSqlSessionFactorybeanName());
        Object[] sqlSessionContructvalues = sqlSessionContext.getSqlSessionContructvalues();
        Object[] sqlSessionContructvalues1 = getSqlSessionContructvalues(sqlSessionContructvalues);
        Class<?>[] sqlSessionContructTypes = getSqlSessionContructTypes(sqlSessionContructvalues);

        BiFunction<SqlSessionFactory, Object[], SqlSession> sqlSessionFactorySqlSessionBiFunction = redisCommandBindings.get(new SqlSessionMethodKey(sqlSessionContructTypes));
        SqlSession session = sqlSessionFactorySqlSessionBiFunction.apply(sqlSessionFactory, sqlSessionContructvalues1);
        return session;
    }

    private Object[] getSqlSessionContructvalues(Object[] sqlSessionContructvalues) {
        for (Object object : sqlSessionContructvalues) {
            if (object == Connection.class) {
            }
        }
        return sqlSessionContructvalues;
    }

    private Class<?>[] getSqlSessionContructTypes(Object[] sqlSessionContructvalues) {
        if (sqlSessionContructvalues == null) {
            return null;
        }
        Class<?>[] classes = new Class[sqlSessionContructvalues.length];
        for (int i = 0; i < classes.length; i++) {
            Object sqlSessionContructvalue = sqlSessionContructvalues[i];
            if (sqlSessionContructvalue == Connection.class) {
                classes[i] = Connection.class;
                continue;
            }
            classes[i] = sqlSessionContructvalue.getClass();
        }
        return classes;
    }


    /**
     * SqlSession
     *
     * @return
     */
    private static Map<SqlSessionMethodKey, BiFunction<SqlSessionFactory, Object[], SqlSession>> initSqlSessionBindings() {
        Class<?> sqlSessionFactoryInterfaceClass = SqlSessionFactory.class;
        Method[] redisCommandMethods = sqlSessionFactoryInterfaceClass.getMethods();
        int length = redisCommandMethods.length;
        Map<SqlSessionMethodKey, BiFunction<SqlSessionFactory, Object[], SqlSession>> redisCommandBindings = new HashMap<>(1);
        for (int i = 0; i < length; i++) {
            Method redisCommandMethod = redisCommandMethods[i];
            initSqlSessionBindings(SqlSession.class, redisCommandMethod, redisCommandBindings);
        }
        return unmodifiableMap(redisCommandBindings);
    }

    private static void initSqlSessionBindings(Class<?> redisCommandInterfaceClass, Method redisCommandMethod, Map<SqlSessionMethodKey, BiFunction<SqlSessionFactory, Object[], SqlSession>> redisCommandBindings) {
        Class<?> returnType = redisCommandMethod.getReturnType();
        if (returnType != redisCommandInterfaceClass) {
            return;
        }
        int parameterCount = redisCommandMethod.getParameterCount();
        if (parameterCount == 0) {
            SqlSessionMethodKey sqlSessionMethodKey = new SqlSessionMethodKey(null);
            BiFunction<SqlSessionFactory, Object[], SqlSession> biFunction = (sqlSessionFactory, objectValuse) -> (SqlSession) ReflectionUtils.invokeMethod(redisCommandMethod, sqlSessionFactory);
            redisCommandBindings.put(sqlSessionMethodKey, biFunction);
            logger.debug("Redis command interface {} Bind RedisConnection command object method {}", sqlSessionMethodKey, biFunction);
            return;
        }
        Class<?>[] parameterTypes = redisCommandMethod.getParameterTypes();
        SqlSessionMethodKey sqlSessionMethodKey = new SqlSessionMethodKey(parameterTypes);
        BiFunction<SqlSessionFactory, Object[], SqlSession> biFunction = (sqlSessionFactory, objectValuse) -> (SqlSession) ReflectionUtils.invokeMethod(redisCommandMethod, sqlSessionFactory, objectValuse);
        redisCommandBindings.put(sqlSessionMethodKey, biFunction);
        logger.debug("Redis command interface {} Bind RedisConnection command object method {}", sqlSessionMethodKey, redisCommandMethod);
    }

    @Override
    public void afterSingletonsInstantiated() {
        this.dbReplicatorConfiguration = applicationContext.getBean(DBReplicatorConfiguration.class);
    }
}
