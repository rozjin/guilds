#!/bin/bash

GRADLE_FORGE_VER="1.12.2-14.23.5.2860"
GRADLE_SPONGE_VER="1.12.2-2838-7.4.7"

GRADLE_FORGE="forge-${GRADLE_FORGE_VER}-installer.jar"
GRADLE_SPONGE="spongeforge-${GRADLE_SPONGE_VER}.jar"

GRADLE_RUN="forge-${GRADLE_FORGE_VER}.jar"

FORGE_URL="https://maven.minecraftforge.net/net/minecraftforge/forge/${GRADLE_FORGE_VER}/${GRADLE_FORGE}"
SPONGE_URL="https://repo.spongepowered.org/repository/maven-releases/org/spongepowered/spongeforge/${GRADLE_SPONGE_VER}/${GRADLE_SPONGE}"

GUILDS_BUILD_JAR=$1
GRADLE_BUILD_DIR=${PWD}
GRADLE_SERVER_DIR=${GRADLE_BUILD_DIR}/server
GRADLE_TMP_DIR=${GRADLE_BUILD_DIR}/tmp

SPONGE_MODS_DIR=${GRADLE_SERVER_DIR}/mods
SPONGE_PLUGIN_DIR=${GRADLE_SERVER_DIR}/plugins
SPONGE_CONFIG=${GRADLE_SERVER_DIR}/config/sponge/global.conf

JAVA_RUN="/usr/lib/jvm/java-8-openjdk-amd64/bin/java"
JAVA_CMD="${JAVA_RUN} -jar"

do_forge() {
    mkdir -p ${GRADLE_SERVER_DIR}
    cd ${GRADLE_SERVER_DIR}

    ${JAVA_CMD} ${GRADLE_TMP_DIR}/${GRADLE_FORGE} --installServer
}

do_gen() {
    cd ${GRADLE_SERVER_DIR}

    ${JAVA_CMD} ${GRADLE_SERVER_DIR}/${GRADLE_RUN} --nogui
}

do_sponge() {
    mkdir -p ${SPONGE_MODS_DIR}
    mkdir -p ${SPONGE_PLUGIN_DIR}

    cp ${GRADLE_TMP_DIR}/${GRADLE_SPONGE} ${SPONGE_MODS_DIR}
    do_gen
    sed -i 's/CANONICAL_MODS_DIR/CANONICAL_GAME_DIR/g' ${SPONGE_CONFIG}
}

do_guilds() {
    echo ${GUILDS_BUILD_JAR}

    cp ${GUILDS_BUILD_JAR} ${SPONGE_PLUGIN_DIR}
}

do_target() {
    if [[ ! -f ${GRADLE_TMP_DIR}/${GRADLE_FORGE} ]]; then
        wget -O ${GRADLE_TMP_DIR}/${GRADLE_FORGE}  ${FORGE_URL}
    fi
    if [[ ! -f ${GRADLE_TMP_DIR}/${GRADLE_SPONGE} ]]; then
        wget -O ${GRADLE_TMP_DIR}/${GRADLE_SPONGE} ${SPONGE_URL}
    fi

    if [[ ! -f ${SPONGE_CONFIG} ]]; then
        do_forge
        do_sponge
    fi

    do_guilds
}

do_target
