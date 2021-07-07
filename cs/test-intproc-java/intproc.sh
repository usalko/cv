#!/bin/bash
./gradlew clean build
java -cp ./shared/build/libs/shared-1.0-SNAPSHOT.jar:./plugins/sort/build/libs/sort-1.0-SNAPSHOT.jar:./plugins/save-html/build/libs/save-html-1.0-SNAPSHOT.jar:./core/build/libs/core-1.0-SNAPSHOT.jar -Dfile.encoding=UTF8 com.usalko.test.intproc.IntProc