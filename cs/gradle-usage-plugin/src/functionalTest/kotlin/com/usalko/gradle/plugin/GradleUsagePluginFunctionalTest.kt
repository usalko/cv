package com.usalko.gradle.plugin

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

import org.gradle.testkit.runner.GradleRunner
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * A simple functional test for the 'com.usalko.gradle.plugin.usage' plugin.
 */
class GradleUsagePluginFunctionalTest {

    @Test fun usageClassTask() {
        // Setup the test build
        val projectDir = File("build/functionalTest")
        projectDir.mkdirs()
        projectDir.resolve("settings.gradle").writeText("")
        projectDir.resolve("build.gradle").writeText("""
            plugins {
                id('com.usalko.gradle-usage-plugin')
                id 'java'
            }
            
            repositories {
                mavenCentral() 
                mavenLocal()
            }
            
            dependencies {
                // Add junit 4 library for tests.
                testImplementation("junit:junit:4.12")
            }
            

        """)

        // Run the build
        val runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("usage-class", "-PclassName=Assert")
        runner.withProjectDir(projectDir)
        val result = runner.build()

        // Verify the result
        assertTrue(result.output.contains("junit:junit:4.12"))
    }

    @Test fun usageResourceTask() {
        // Setup the test build
        val projectDir = File("build/functionalTest")
        projectDir.mkdirs()
        projectDir.resolve("settings.gradle").writeText("")
        projectDir.resolve("build.gradle").writeText("""
            plugins {
                id('com.usalko.gradle-usage-plugin')
                id 'java'
            }
            
            repositories {
                mavenCentral() 
                mavenLocal()
            }
            
            dependencies {
                // Add junit 4 library for tests.
                testImplementation("junit:junit:4.12")
            }

        """)

        // Run the build
        val runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("usage-resource", "-PresourceName=MANIFEST")
        runner.withProjectDir(projectDir)
        val result = runner.build()

        // Verify the result
        assertTrue(result.output.contains("\"artifact\":\"junit.jar (junit:junit:4.12)\",\"entry\":\"META-INF/MANIFEST.MF\""))
    }

}
