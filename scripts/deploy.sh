#!/bin/bash

REPOSITORY=/home/ec2-user/app/web-app
PROJECT_NAME=siders-webapp

cd $REPOSITORY/$PROJECT_NAME

echo "> Build 파일 복사"

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 현재 구동 중인 애플리케이션 pid 확인"

CURRENT_PID=$(pgrep -fl siders-webapp | grep java | awk '{print $1}')

echo "> 현재 구동 중인 애플리케이션의 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 10
fi

# 자꾸 8080포트가 중복되었다고 뜬다. 일단 아래의 구문을 추가해서 한번 더 검사하자. 그래도 문제 발생 시 다른 해결방안 ㄱㄱ
if [ -z "$CURRENT_PID" ]; then
  echo "> 프로세스가 정상적으로 종료되었습니다."
else
  echo "> kill -9 $CURRENT_PID"
  kill -9 $CURRENT_PID
  sleep 10
fi

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | grep -v "plain" | tail -n 1)

echo "> JAR_NAME: $JAR_NAME"

echo "> $JAR_NAME 에 실행 권한을 추가합니다."

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

nohup java -jar \
        -Dspring.config.location=classpath:/application.yml,/home/ec2-user/app/application-ec2-db.yml,/home/ec2-user/app/application-jwt.yml,/home/ec2-user/app/application-oauth.yml,classpath:/application-ec2.yml \
        -Dspring.profile.active=ec2 \
$JAR_NAME > $REPOSITORY/nohup.out 2>&1 &