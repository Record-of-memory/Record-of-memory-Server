#!/bin/bash

REPOSITORY=/home/ubuntu/app
cd $REPOSITORY

# log를 저장할 파일 생성
APP_LOG=$REPOSITORY/application.log
ERROR_LOG=$REPOSITORY/error.log
DEPLOY_LOG=$REPOSITORY/deploy.log

# 1) 애플리케이션이 구동중인지 확인하기 위한 애플리케이션 이름, jar 파일의 이름
APP_NAME=RecordOfMemory
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep ‘.jar’ | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

# 2) 현재 인스턴스에서 애플리케이션이 구동중인지 확인, 구동중이면 종료
CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ]
then
	echo “> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다.”
else
	echo “> kill -15 $CURRENT_PID”
	sudo kill -15 $CURRENT_PID
	sleep 5
fi

# 3) jar 파일을 배포
echo “> $JAR_PATH 배포”
nohup java -jar $JAR_PATH > $APP_LOG 2> $ERROR_LOG &
