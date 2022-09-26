#!/usr/bin/env bash

function find_idle_profile() {
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)

    if [ ${RESPONSE_CODE} -ge 400 ];
    then
      CURRENT_PROFILE=ec2-nginx2
    else
      CURRENT_PROFILE=$(curl -s http://localhost/profile)
    fi

    if [ ${CURRENT_PROFILE} == ec2-nginx1 ];
    then
      IDLE_PROFILE=ec2-nginx2
    else
      IDLE_PROFILE=ec2-nginx1
    fi

    echo "${IDLE_PROFILE}"
}

function find_idle_port() {
    IDLE_PROFILE=$(find_idle_profile)

    if [ ${IDLE_PROFILE} == ec2-nginx1 ];
    then
      echo "8081"
    else
      echo "8082"
    fi
}


