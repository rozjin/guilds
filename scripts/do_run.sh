#!/bin/bash

GRADLE_BUILD_DIR=${PWD}
GRADLE_SERVER_DIR=${GRADLE_BUILD_DIR}/server

GRADLE_FORGE_VER="1.12.2-14.23.5.2860"
GRADLE_RUN="forge-${GRADLE_FORGE_VER}.jar"

JAVA_RUN="/usr/lib/jvm/java-8-openjdk-amd64/bin/java"
JAVA_CMD="${JAVA_RUN} -jar"
do_run() {
    cd ${GRADLE_SERVER_DIR}

    ${JAVA_CMD} ${GRADLE_SERVER_DIR}/${GRADLE_RUN} --nogui
}

do_target() {
    cd ${GRADLE_SERVER_DIR}

    ${JAVA_CMD} ${GRADLE_SERVER_DIR}/${GRADLE_RUN} --nogui
}

do_target
