/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

val projectVersion: String by project
project.version = projectVersion
val asmVersion: String by project
val version = "${project.version}"
val group = "com.usalko"


plugins {
    // Apply the Java Gradle plugin development plugin to add support for developing Gradle plugins
    `java-gradle-plugin`

    // Publish to local maven repository
    `maven-publish`

    // Publish to gradle repository
    id("com.gradle.plugin-publish") version "0.10.1"

    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.3.50"
}

publishing {
    publications {
        create<MavenPublication>("pluginMaven") {
            groupId = "com.usalko"
            artifactId = "gradle-usage-plugin"
            version = "${project.version}"

            //from(components["java"])
        }
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Use the Kotlin JDK 8 standard library.
    implementation("org.ow2.asm:asm:$asmVersion")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

gradlePlugin {
    // Define the plugin
    val usage by plugins.creating {
        id = "com.usalko.gradle-usage-plugin"
        implementationClass = "com.usalko.gradle.plugin.GradleUsagePlugin"
    }
    println(usage.id)
}

pluginBundle {
    // These settings are set for the whole plugin bundle
    website = "http://usalko.com/"
    vcsUrl = "https://github.com/usalko/gradle-usage-plugin.git"

    description = "Gradle-usage-plugin - simple plugin for collect information" +
            " about usage classes or resources. Plugin give that info from dependency tree."

    (plugins) {

        // first plugin
        "usage" {
            // id is captured from java-gradle-plugin configuration
            displayName = "Gradle Greeting plugin"
            tags = listOf("individual", "tags", "per", "plugin")
            version = "${project.version}"
        }

    }

    mavenCoordinates {
        groupId = "com.usalko"
        artifactId = "gradle-usage-plugin"
        version = "${project.version}"
    }
}
// Add a source set for the functional test suite
val functionalTestSourceSet = sourceSets.create("functionalTest") {
}

gradlePlugin.testSourceSets(functionalTestSourceSet)
configurations.getByName("functionalTestImplementation").extendsFrom(configurations.getByName("testImplementation"))

// Add a task to run the functional tests
val functionalTest by tasks.creating(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
}

val check by tasks.getting(Task::class) {
    // Run the functional tests as part of `check`
    dependsOn(functionalTest)
}
