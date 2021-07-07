###### USAGE MAVEN PLUGIN

Simple plugin with two goals:

- usage:class For search substring for class-name in code and collect statistics after walking by dependency tree
- usage:resource For search substring for resource-name in dependency tree

Both goals return json result into log

##

Example for class goal, if you apply in this project directory:
```text
mvn com.usalko:usage-maven-plugin:class -DclassName=LoggerFactory
```
Output will be include json:
```text
[INFO] {"usage-class": [{"artifact":"org.apache.maven.resolver:maven-resolver-transport-file:jar:1.0.3:test","entry":"org/eclipse/aether/transport/file/FileTransporterFactory:6"},
 {"artifact":"org.apache.maven.resolver:maven-resolver-spi:jar:1.0.3:test","entry":"org/eclipse/aether/spi/log/NullLoggerFactory:11"},
 {"artifact":"org.apache.maven.resolver:maven-resolver-connector-basic:jar:1.0.3:test","entry":"org/eclipse/aether/connector/basic/BasicRepositoryConnectorFactory:6"},
 {"artifact":"org.apache.maven.resolver:maven-resolver-transport-http:jar:1.0.3:test","entry":"org/eclipse/aether/transport/http/HttpTransporterFactory:6"},
 {"artifact":"org.slf4j:jcl-over-slf4j:jar:1.6.2:test","entry":"org/apache/commons/logging/impl/SLF4JLocationAwareLog:1"},
 {"artifact":"org.slf4j:jcl-over-slf4j:jar:1.6.2:test","entry":"org/apache/commons/logging/impl/SLF4JLog:1"},
 {"artifact":"org.slf4j:jcl-over-slf4j:jar:1.6.2:test","entry":"org/apache/commons/logging/impl/SLF4JLogFactory:1"},
 {"artifact":"org.slf4j:slf4j-api:jar:1.6.2:test","entry":"org/slf4j/helpers/NamedLoggerBase:1"},
 {"artifact":"org.slf4j:slf4j-api:jar:1.6.2:test","entry":"org/slf4j/helpers/NOPLoggerFactory:2"},
 {"artifact":"org.slf4j:slf4j-api:jar:1.6.2:test","entry":"org/slf4j/helpers/SubstituteLoggerFactory:8"},
 {"artifact":"org.slf4j:slf4j-api:jar:1.6.2:test","entry":"org/slf4j/LoggerFactory:47"},
 {"artifact":"org.eclipse.sisu:org.eclipse.sisu.plexus:jar:0.0.0.M5:test","entry":"org/codehaus/plexus/DefaultPlexusContainer$SLF4JLoggerFactoryProvider:5"},
 {"artifact":"org.eclipse.sisu:org.eclipse.sisu.plexus:jar:0.0.0.M5:test","entry":"org/codehaus/plexus/DefaultPlexusContainer:3"},
 {"artifact":"org.eclipse.sisu:org.eclipse.sisu.plexus:jar:0.0.0.M5:test","entry":"org/eclipse/sisu/plexus/PlexusLifecycleManager:3"},
 {"artifact":"org.sonatype.sisu:sisu-guice:jar:no_aop:3.1.0:test","entry":"com/google/inject/internal/InjectorShell$LoggerFactory:11"},
 {"artifact":"org.sonatype.sisu:sisu-guice:jar:no_aop:3.1.0:test","entry":"com/google/inject/internal/InjectorShell$SLF4JLoggerFactory:25"},
 {"artifact":"org.sonatype.sisu:sisu-guice:jar:no_aop:3.1.0:test","entry":"com/google/inject/internal/InjectorShell:8"},
 {"artifact":"org.eclipse.sisu:org.eclipse.sisu.inject:jar:0.0.0.M5:test","entry":"org/eclipse/sisu/inject/Logs$SLF4JSink:1"}]}
```
##
Example for resource goal, if you apply in this project directory:
```text
mvn com.usalko:usage-maven-plugin:resource -DresourceName=package-info
```
Output will be include json:
```text
[INFO] {"usage-resource": [{"artifact":"org.eclipse.sisu:org.eclipse.sisu.plexus:jar:0.0.0.M5:test","entry":"org/eclipse/sisu/plexus/package-info.class"},
 {"artifact":"com.google.guava:guava:jar:10.0.1:test","entry":"com/google/common/collect/package-info.class"},
 {"artifact":"com.google.guava:guava:jar:10.0.1:test","entry":"com/google/common/cache/package-info.class"},
 {"artifact":"com.google.guava:guava:jar:10.0.1:test","entry":"com/google/common/base/package-info.class"},
 {"artifact":"com.google.guava:guava:jar:10.0.1:test","entry":"com/google/common/net/package-info.class"},
 {"artifact":"com.google.guava:guava:jar:10.0.1:test","entry":"com/google/common/io/package-info.class"},
 {"artifact":"com.google.guava:guava:jar:10.0.1:test","entry":"com/google/common/primitives/package-info.class"},
 {"artifact":"com.google.guava:guava:jar:10.0.1:test","entry":"com/google/common/util/concurrent/package-info.class"},
 {"artifact":"org.eclipse.sisu:org.eclipse.sisu.inject:jar:0.0.0.M5:test","entry":"org/eclipse/sisu/bean/package-info.class"},
 {"artifact":"org.eclipse.sisu:org.eclipse.sisu.inject:jar:0.0.0.M5:test","entry":"org/eclipse/sisu/inject/package-info.class"},
 {"artifact":"org.eclipse.sisu:org.eclipse.sisu.inject:jar:0.0.0.M5:test","entry":"org/eclipse/sisu/launch/package-info.class"},
 {"artifact":"org.eclipse.sisu:org.eclipse.sisu.inject:jar:0.0.0.M5:test","entry":"org/eclipse/sisu/package-info.class"},
 {"artifact":"org.eclipse.sisu:org.eclipse.sisu.inject:jar:0.0.0.M5:test","entry":"org/eclipse/sisu/space/package-info.class"},
 {"artifact":"org.eclipse.sisu:org.eclipse.sisu.inject:jar:0.0.0.M5:test","entry":"org/eclipse/sisu/wire/package-info.class"},
 {"artifact":"org.eclipse.sisu:org.eclipse.sisu.inject:jar:0.0.0.M5:test","entry":"org/sonatype/inject/package-info.class"}]}s
```
