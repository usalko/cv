###### GRADLE USAGE PLUGIN

Simple plugin with two tasks:

- usage-class For search substring for class-name in code and collect statistics after walking by dependency tree
- usage-resource For search substring for resource-name in dependency tree

Both tasks return json result into log. If you not have to include this plugin in the project, use usage-plugin.gradle snippet from this project.

##

Example for class task, if you apply in this project directory:
```text
./gradlew --init-script usage-plugin.gradle usage-class -DclassName=ArrayChannel
```
Output will be include json:
```text
{"usage-class": [{"artifact":"kotlin-compiler-embeddable.jar (org.jetbrains.kotlin:kotlin-compiler-embeddable:1.3.50)","entry":"org/jetbrains/kotlin/kotlinx/coroutines/channels/ArrayChannel:95"},
 {"artifact":"kotlin-compiler-embeddable.jar (org.jetbrains.kotlin:kotlin-compiler-embeddable:1.3.50)","entry":"org/jetbrains/kotlin/kotlinx/coroutines/channels/ChannelKt:2"},
 {"artifact":"kotlinx-coroutines-core.jar (org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.1)","entry":"kotlinx/coroutines/channels/ArrayChannel:95"},
 {"artifact":"kotlinx-coroutines-core.jar (org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.1)","entry":"kotlinx/coroutines/channels/ChannelKt:2"}], "resource-name": "ArrayChannel"}
```
##
Example for resource task, if you apply in this project directory:
```text
./gradlew --init-script usage-plugin.gradle usage-resource -DresourceName=LICENSE
```
Output will be include json:
```text
{"usage-resource": [{"artifact":"junit.jar (junit:junit:4.12)","entry":"LICENSE-junit.txt"},
 {"artifact":"hamcrest-core.jar (org.hamcrest:hamcrest-core:1.3)","entry":"LICENSE.txt"}], "resource-name": "LICENSE"}
```
