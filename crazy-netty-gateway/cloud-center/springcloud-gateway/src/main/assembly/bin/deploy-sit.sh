#!/bin/bash

#服务参数
SERVER_PORT=7790


PRO_NAME="springcloud-gateway"
WORK_PATH="/work"

# export log_springcloud_gateway_demo_path="${WORK_PATH}/logs"
# export NACOS_SERVER=cdh1:8848
# 向hosts文件追加内容
# echo "192.168.56.121 cdh1"  >> /etc/hosts
# echo "192.168.56.122 cdh1"  >> /etc/hosts
# echo "192.168.56.123 cdh3"  >> /etc/hosts

MAIN_CLASS="com.crazymaker.cloud.nacos.demo.gateway.starter.GatewayProviderApplication"
JAR_NAME="/app/${PRO_NAME}.jar"

#BOOT_STRAP_YML= "-Dspring.config.location=bootstrap.yaml"


LOG="${WORK_PATH}/logs/console.log"
PROFILES_ACTIVE="-Dspring.profiles.active=sit"
APPLICATION_CONFIG="-Dserver.port=${SERVER_PORT} ${PROFILES_ACTIVE} -Duser.timezone=GMT+08"
REMOTE_CONFIG="-Xdebug -Xrunjdwp:transport=dt_socket,address=5005,server=y,suspend=n"


SKYWALKING_CONFIG="-javaagent:${SKYWALKING_JAR_PATH} -Dskywalking.agent.service_name=${PRO_NAME} -Dskywalking.collector.backend_service=${SKYWALKING_RPC_ADDRESS}"

#START_CMD="nohup java ${JVM_CONF} ${SKYWALKING_CONFIG}  ${APPLICATION_CONFIG}  -jar ${JAR_NAME} ${MAIN_CLASS}  &"


if [ "$SKYWALKING_JAR_PATH" != "" ]; then
   START_CMD="java ${JVM_CONF} ${SKYWALKING_CONFIG}  ${APPLICATION_CONFIG}  -jar ${JAR_NAME} ${MAIN_CLASS}"
else
   START_CMD="java ${JVM_CONF} ${APPLICATION_CONFIG}  -jar ${JAR_NAME} ${MAIN_CLASS}"
fi

RETVAL="0"

# See how we were called.
function start() {
    echo " start  ${PRO_NAME} .........."
    echo "PORT:$SERVER_PORT"
    echo "JVM_CONF:$JVM_CONF"
    echo "START_CMD： $START_CMD"
    echo "log_springcloud_gateway_path:$log_springcloud_gateway_path"

    #   nohup java ${JVM} ${APPLICATION_CONFIG}  -jar ${WORK_PATH}/lib/${JAR_NAME} ${MAIN_CLASS} >> ${LOG} 2>&1 &
    # nohup java ${JVM} ${SKYWALKING_CONFIG}  ${APPLICATION_CONFIG}  ${BOOT_STRAP_YML}   -jar ${JAR_NAME} ${MAIN_CLASS}  &
    ${START_CMD}
    status
}


function stop() {
    pid=$(ps -ef | grep -v 'grep' | egrep $JAR_NAME| awk '{printf $2 " "}')
    if [ "$pid" != "" ]; then
        echo -n $"Shutting down boot: "
        kill -9 "$pid"
    else
        echo "${JAR_NAME} is stopped"
    fi
    status
}

function debug() {
    echo " start remote debug mode .........."
    if [ ! -f ${LOG} ]; then
        touch ${LOG}
    fi
        nohup java ${JVM} ${APPLICATION_CONFIG} ${REMOTE_CONFIG}  -jar ${WORK_PATH}/lib/${JAR_NAME} ${MAIN_CLASS} >> ${LOG} 2>&1 &
}

function status(){
    pid=$(ps -ef | grep -v 'grep' | egrep $JAR_NAME| awk '{printf $2 " "}')
    #echo "$pid"
    if [ "$pid" != "" ]; then
        echo "${JAR_NAME} is running,pid is $pid"
    else
        echo "${JAR_NAME} is stopped"
    fi
}

function usage(){
    echo "Usage: $0 {start|debug|stop|restart|status}"
    RETVAL="2"
}

# See how we were called.
case "$1" in
    start)
        start
    ;;
    debug)
        debug
    ;;
    stop)
        stop
    ;;
    restart)
        stop
    	start
    ;;
    status)
        status
    ;;
    *)
        usage
    ;;
esac

exit ${RETVAL}