#!/bin/bash
./gradlew clean build publishToMavenLocal
EXIT_CODE=$?
if [[ "${EXIT_CODE}x" != "0x" ]]; then
	exit $EXIT_CODE
fi
./gradlew --init-script usage-plugin.gradle usage-class -DclassName=ArrayChannel
./gradlew --init-script usage-plugin.gradle usage-resource -DresourceName=LICENSE

