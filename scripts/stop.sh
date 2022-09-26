#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)  # 해당 파일의 절대 경로
ABSDIR=$(dirname $ABSPATH)  # 해당 파일의 절대 경로에 해당하는 디렉토리 이름
source ${ABSDIR}/profile.sh  # 해당 파일의 절대 경로에 해당하는 디렉토리를 찾고, 그 디렉토리 안에 있을 profile.sh를 import

IDLE_PORT=$(find_idle_port)

echo "> $IDLE_PORT 에서 구동 중인 애플리케이션의 pid 확인"
IDLE_PID=$(lsof -ti tcp:${IDLE_PORT})

if [ -z ${IDLE_PID} ];
then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $IDLE_PID"
  kill -15 ${IDLE_PID}
  sleep 5
fi
