#!/bin/bash
######################################################################
# 服务启动脚本模板
# by James
# 2013-10-08
######################################################################

CUR_DIR=`pwd`
CLASSPATH="$CLASSPATH:$CUR_DIR:."
LANG="zh_CN.UTF-8"
#JAVA_HOME="/opt/web_app/jdk"

JAVA_OPS="-server -Xmx3024m -Xms3024m "

APP_PACKAGE_NAME="com.ctfo.main.TestSaveCenterMain"


export LANG

function startServer() {

        for _jar in ./*.jar; do
                CLASSPATH="$CLASSPATH:${_jar}"
        done
        
        rm -rf /logs/supp_app/TestSaveCenter*
        mkdir -p /logs/supp_app/TestSaveCenter
        $JAVA_HOME/bin/java $JAVA_OPS -cp $CLASSPATH $APP_PACKAGE_NAME >> /logs/supp_app/TestSaveCenter/console.log 2>&1 &
        echo "server runing ......"
        tail -100f /logs/supp_app/TestSaveCenter/console.log
}

function restart() {
	APP_PID=`jps -l | grep $APP_PACKAGE_NAME | cut -d' ' -f 1`

	if [ -n "$APP_PID" ]; then
		kill -9 "$APP_PID"
		echo "Kill PID: $APP_PID  OK!"
		echo "ready start ..."
		startServer
	fi
}

function tailLog() {
        echo "tail -100f /logs/supp_app/TestSaveCenter/TestSaveCenter.log ......"
        tail -100f /logs/supp_app/TestSaveCenter/TestSaveCenter.log
}

function usage() {
	echo "Usage: $0 [start]    @_@"
	exit 1
}

if [  $# -ne 1  ]; then
	usage
fi

case $1 in
	start) startServer;;
	log) tailLog;;
	restart) restart;;
	*) usage;;
esac

