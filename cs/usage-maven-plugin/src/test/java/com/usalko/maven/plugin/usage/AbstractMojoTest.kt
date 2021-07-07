package com.usalko.maven.plugin.usage

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

import junit.framework.TestCase
import org.apache.maven.execution.DefaultMavenExecutionRequest
import org.apache.maven.execution.DefaultMavenExecutionResult
import org.apache.maven.execution.MavenExecutionRequestPopulator
import org.apache.maven.execution.MavenSession
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.Mojo
import org.apache.maven.plugin.descriptor.MojoDescriptor
import org.apache.maven.plugin.descriptor.PluginDescriptor
import org.apache.maven.plugin.logging.Log
import org.apache.maven.plugin.testing.AbstractMojoTestCase
import org.apache.maven.plugin.testing.SilentLog
import org.apache.maven.plugins.annotations.Component
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.DefaultProjectBuildingRequest
import org.apache.maven.project.MavenProject
import org.apache.maven.project.ProjectBuilder
import org.apache.maven.repository.internal.MavenRepositorySystemSession
import org.apache.maven.shared.model.fileset.FileSet
import org.apache.maven.shared.model.fileset.util.FileSetManager
import org.codehaus.plexus.PlexusTestCase
import org.codehaus.plexus.configuration.PlexusConfiguration
import org.codehaus.plexus.util.StringUtils
import org.sonatype.aether.RepositorySystemSession
import org.sonatype.aether.impl.internal.SimpleLocalRepositoryManager
import java.io.File
import java.io.IOException
import java.lang.reflect.Field
import java.util.*

abstract class AbstractMojoTest : AbstractMojoTestCase() {

    companion object TestUtils {
        /**
         * Deletes a directory and its contents.
         *
         * @param dir [File] The base directory of the included and excluded files.
         * @throws IOException in case of an error. When a directory failed to get deleted.
         */
        @Throws(IOException::class)
        fun removeDirectory(dir: File?){
            if (dir != null) {
                val log = SilentLog()
                val fileSetManager = FileSetManager(log as Log, false)

                val fs = FileSet()
                fs.directory = dir.path
                fs.addInclude("**/**")
                fileSetManager.delete(fs)
            }
        }

        /**
         * clean up configuration string before it can be tokenized
         * @param str The str which should be cleaned.
         * @return cleaned up string.
         */
        fun cleanToBeTokenizedString(str: String): String {
            var ret = ""
            if (!StringUtils.isEmpty(str)) {
                // remove initial and ending spaces, plus all spaces next to commas
                ret = str.trim { it <= ' ' }.replace("[\\s]*,[\\s]*".toRegex(), ",")
            }

            return ret
        }

    }

    protected lateinit var session: MavenSession
    protected var project: MavenProject? = null
    protected var pluginConfiguration: PlexusConfiguration? = null

    protected var testDir: File? = null

    protected var stubFactory: UsageArtifactStubFactory? = null

    @Throws(Exception::class)
    protected fun setUp(testDirStr: String, createFiles: Boolean) {
        setUp(testDirStr, createFiles, true)
    }

    @Throws(Exception::class)
    protected fun setUp(testDirStr: String, createFiles: Boolean, flattenedPath: Boolean) {
        // required for mojo lookups to work
        super.setUp()
        testDir = File(PlexusTestCase.getBasedir(), "target" + File.separatorChar + "unit-tests" + File.separatorChar + testDirStr
                + File.separatorChar)
        removeDirectory(testDir)
        TestCase.assertFalse(testDir!!.exists())

        stubFactory = UsageArtifactStubFactory(this.testDir as File, createFiles, flattenedPath)
    }

    override fun tearDown() {
        if (testDir != null) {
            try {
                removeDirectory(testDir)
            } catch (e: IOException) {
                e.printStackTrace()
                TestCase.fail("Trying to remove directory:$testDir\r\n$e")
            }

            TestCase.assertFalse(testDir!!.exists())

            testDir = null
        }

        stubFactory = null
    }

//    @Throws(MojoExecutionException::class)
//    fun copyFile(mojo: AbstractUsageMojo, artifact: File, destFile: File) {
//        println(mojo) // Block warning for not-usage parameter
//        FileUtils.copyFile(artifact, destFile)
//    }

    fun <T> configureExecutionSession(pomForTestsPath: String,
                                  groupId: String,
                                  version: String,
                                  artifactId: String,
                                  goal: String,
                                  implementationClass: Class<T>): File {
        //val mavenSettingsBuilder = container.lookup(MavenSettingsBuilder.ROLE) as MavenSettingsBuilder
        //val settings = mavenSettingsBuilder.buildSettings()
        val request = DefaultMavenExecutionRequest()

        // CHECK POM DESCRIPTOR
        val pomForTests = getTestFile(pomForTestsPath)
        assertNotNull(pomForTests)
        assertTrue(pomForTests.exists())

        request.pom = pomForTests
        //request.setLocalRepositoryPath(settings.localRepository)

        val populator = container.lookup(MavenExecutionRequestPopulator::class.java)
        populator.populateDefaults(request)

        val projectBuilder = lookup(ProjectBuilder::class.java)
        val buildingRequest = DefaultProjectBuildingRequest()
        project = projectBuilder.build(pomForTests, buildingRequest).project
        //noinspection deprecation - wait on maven-plugin-testing-harness update
        val mavenExecutionResult = DefaultMavenExecutionResult()
        val repositorySession: RepositorySystemSession = MavenRepositorySystemSession()
        session = MavenSession(container, repositorySession, request, mavenExecutionResult)
        session.currentProject = project
        session.projects = Collections.singletonList(project)
        request.systemProperties = System.getProperties()

        //val reactorProjects = session.projects

        pluginConfiguration = extractPluginConfiguration(artifactId, pomForTests)
        val mojoDescriptor = MojoDescriptor()
        val pluginDescriptor = PluginDescriptor()
        pluginDescriptor.artifactId = artifactId
        pluginDescriptor.groupId = groupId
        pluginDescriptor.version = version
        mojoDescriptor.pluginDescriptor = pluginDescriptor
        //mojoDescriptor.implementationClass =
        mojoDescriptor.setImplementationClass(implementationClass)
        mojoDescriptor.goal = goal
        val roleClass = Mojo::class.java
        mojoDescriptor.role = roleClass.canonicalName
        mojoDescriptor.roleHint = "$groupId:$artifactId:$version:$goal"

        val systemSession = session.repositorySession as MavenRepositorySystemSession
        systemSession.localRepositoryManager = SimpleLocalRepositoryManager(File(System.getProperty("user.home") +"/.m2/repository"))

        return pomForTests
    }

    /**
     * If you know more convenient way - rewrite this method
     * Ugly, but if you can more convenient way, rewrite or cut below code please
     */
    fun customPlexusResolver(mojo: Mojo,
                             pluginConfiguration: PlexusConfiguration?,
                             project: MavenProject?,
                             session: MavenSession) {
        val config = pluginConfiguration!!.children.map { it.name to it.value }.toMap()

        allDeclaredFields(mojo::class.java, AbstractMojo::class.java).forEach {
            injectFieldValue(mojo, it, config, project, session)
        }
    }

    private fun injectFieldValue(mojo: Mojo,
                                 mojoField: Field,
                                 pluginConfiguration: Map<String, String>,
                                 project: MavenProject?,
                                 session: MavenSession) {
        val mojoFieldAnnotations = mojoField.annotations.toList()
        val componentAnnotation =
                mojoFieldAnnotations.find { it -> it::javaClass == Component::class.java }
        if (componentAnnotation != null) {
            mojoField.set(mojo, container.lookup(mojoField.type::class.java))
            return
        }

        val parameterAnnotation =
                mojoFieldAnnotations.find { it -> it::javaClass == Parameter::class.java }
                        as Parameter?
        if (parameterAnnotation != null) {
            when (parameterAnnotation.property) {
                "\${session}" -> mojoField.set(mojo, session)
                "\${project}" -> mojoField.set(mojo, project)
                else -> mojoField.set(mojo,
                        pluginConfiguration[parameterAnnotation.property])
            }
            return
        }

        // Probably kotlin issue, but it must handling for tests

        if (container.hasComponent(mojoField.type)) {
            mojoField.set(mojo, container.lookup(mojoField.type, "maven3"))
            return
        }

        if (mojoField.name == "project") {
            mojoField.set(mojo, project)
            return
        }

        if (mojoField.name == "session") {
            mojoField.set(mojo, session)
            return
        }

        mojoField.set(mojo, pluginConfiguration[mojoField.name])

    }

    private fun allDeclaredFields(mojoClass: Class<*>, stopClass: Class<AbstractMojo>): List<Field> {
        if (mojoClass == stopClass) {
            return Collections.emptyList()
        }
        val result: MutableList<Field> = mutableListOf()
        result.addAll(allDeclaredFields(mojoClass.superclass, stopClass))

        mojoClass.declaredFields.forEach {
            it.isAccessible = true
            result.add(it)
        }

        return result
    }

}