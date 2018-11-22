#!/usr/bin/env bash
# 1. get source code from gitlab
PROJECT=mini-fc
WEBAPP_NAME=mini-fc-admin
BRANCH=dev
SOURCE_DIR=/home/source/
TOMCAT_PATH=/home/server/tomcat-minifc/

if [ $# -gt 0 ]; then
    BRANCH="$1"
fi


PROJECT_REPO="git@github.com:g4seek/mini-fc.git"
echo ${PROJECT_REPO}

cd ${SOURCE_DIR}

if [ -d ${PROJECT}/.git ]; then
cd ${PROJECT}
git pull && git checkout ${BRANCH}
else
git clone ${PROJECT_REPO} && cd ${PROJECT} && git checkout ${BRANCH}
fi

# 2. build project with maven
cd ${SOURCE_DIR}/${PROJECT}/${WEBAPP_NAME}
mvn clean package

# 3. copy to webapps directory
BUILD_TARGET_PATH=${SOURCE_DIR}/${PROJECT}/${WEBAPP_NAME}/target/${WEBAPP_NAME}
DEPLOY_PATH=/home/webapps/${WEBAPP_NAME}/

if [ ! -d /home/webapps/${WEBAPP_NAME} ]; then
mkdir /home/webapps/${WEBAPP_NAME}
fi

if [ -d ${DEPLOY_PATH} ]; then
rm -rf ${DEPLOY_PATH}
fi

cp -r ${BUILD_TARGET_PATH} ${DEPLOY_PATH}

# 4. restart tomcat
${TOMCAT_PATH}/bin/shutdown.sh
${TOMCAT_PATH}/bin/startup.sh