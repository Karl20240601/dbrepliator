#!/bin/bash

#服务参数
SERVER_PORT=7777


PRO_NAME="springcloud-gateway-demo-1.0-SNAPSHOT"
JAR_NAME="${PRO_NAME}.jar"
WORK_PATH="/work/${PRO_NAME}"
MAIN_CLASS="com.crazymaker.cloud.nacos.demo.gateway.starter.GatewayProviderApplication"
JVM="-server -Xms64m -Xmx256m"
export log_springcloud_gateway_demo_path=/work/logs/${PRO_NAME}
export NACOS_SERVER="cdh1:8848"


LOG="${WORK_PATH}/logs/console.log"
APPLICATION_CONFIG="-Dserver.port=${SERVER_PORT} "
REMOTE_CONFIG="-Xdebug -Xrunjdwp:transport=dt_socket,address=5005,server=y,suspend=n"

echo "PORT:$SERVER_PORT"
echo "JVM:$JVM"

SKYWALKING_CONFIG="-javaagent:${SKYWALKING_JAR_PATH} -Dskywalking.agent.service_name=${PRO_NAME} -Dskywalking.collector.backend_service=${SKYWALKING_RPC_ADDRESS}"


RETVAL="0"

# See how we were called.
function start() {
    if [ ! -f ${LOG} ]; then
        touch ${LOG}
    fi
     #   nohup java ${JVM} ${APPLICATION_CONFIG}  -jar ${WORK_PATH}/lib/${JAR_NAME} ${MAIN_CLASS} >> ${LOG} 2>&1 &
        nohup java ${JVM} ${SKYWALKING_CONFIG}  ${APPLICATION_CONFIG}  -jar ${WORK_PATH}/lib/${JAR_NAME} ${MAIN_CLASS}  &
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