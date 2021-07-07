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

import com.usalko.gradle.plugin.GradleUsageException.TypicalReason.CLASS_ENTRY_ERROR
import com.usalko.gradle.plugin.asm.UsageClassVisitor
import com.usalko.gradle.plugin.asm.VisitorsContext
import com.usalko.utils.Enumerations
import com.usalko.utils.JsonTools
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.logging.Logging
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassReader.EXPAND_FRAMES
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.stream.Collectors

/**
 * Usage plugin.
 */
class GradleUsagePlugin: Plugin<Project> {

    private val resolvedArtifacts = mutableSetOf<String>()

    private val logger = Logging.getLogger(GradleUsagePlugin::class.java)

    override fun apply(project: Project) {
        // Register a usage-class task
        project.tasks.register("usage-class") { task ->
            task.doLast {
                try {
                    usageClassTask(project)
                } catch (e: Exception) {
                    logger.error(e.localizedMessage, e)
                }
            }
        }
        // Register a usage-resource task
        project.tasks.register("usage-resource") { task ->
            task.doLast {
                try {
                    usageResourceTask(project)
                } catch (e: Exception) {
                    logger.error(e.localizedMessage, e)
                }
            }
        }
    }

    private fun usageClassTask(project: Project) {
        val lookupResult = mutableListOf<ResolvedArtifactEntry>()
        val className = (project.properties["className"] as String?)
                ?: System.setProperty("className" ,"Integer")
        project.configurations.forEach { configuration ->
            if (!configuration.isCanBeResolved) {
                return@forEach
            }
            configuration.resolve()
            configuration.resolvedConfiguration.resolvedArtifacts.forEach { resolvedArtifact ->
                perform(resolvedArtifact) { jarEntry: JarEntry, jarEntryContent: ByteArray ->

                    if (!jarEntry.isDirectory &&
                            jarEntry.name.endsWith(".class")) {
                        val classReader = ClassReader(jarEntryContent)
                        val context = VisitorsContext(className)
                        classReader.accept(UsageClassVisitor(context),
                                EXPAND_FRAMES)
                        context.getStatistics().forEach { (clsName, countOfUsage) ->
                            lookupResult.add(ResolvedArtifactEntry(resolvedArtifact, "$clsName:$countOfUsage"))
                        }
                    }

                }
            }
        }

        println("{\"usage-class\": [" + lookupResult.stream().map { it.toString() }
                .collect(Collectors.joining(",\n ")) +
                "], \"resource-name\": ${JsonTools.jsonString(className)}}")
    }

    private fun usageResourceTask(project: Project) {
        val lookupResult = mutableListOf<ResolvedArtifactEntry>()
        val resourceName = (project.properties["resourceName"] as String?)
                ?: System.setProperty("resourceName" ,"MANIFEST")
        project.configurations.forEach { configuration ->
            if (!configuration.isCanBeResolved) {
                return@forEach
            }
            configuration.resolve()
            configuration.resolvedConfiguration.resolvedArtifacts.forEach { resolvedArtifact ->
                perform(resolvedArtifact) { jarEntry: JarEntry, _: ByteArray ->

                    if (!jarEntry.isDirectory &&
                            jarEntry.name.contains(resourceName)) {
                        lookupResult.add(ResolvedArtifactEntry(resolvedArtifact, jarEntry.name))
                    }

                }
            }
        }

        println("{\"usage-resource\": [" + lookupResult.stream().map { it.toString() }
                .collect(Collectors.joining(",\n ")) +
                "], \"resource-name\": ${JsonTools.jsonString(resourceName)}}")
    }

    private fun perform(resolvedArtifact: ResolvedArtifact,
                        handler: (JarEntry, ByteArray) -> Unit) {
        // OPTIMIZATION FOR EXCLUDE DOUBLE CHECK
        if (alwaysChecked(resolvedArtifact)) {
            return
        }

        JarFile(resolvedArtifact.file).use { jarFile ->
            Enumerations.asStream(jarFile.entries()).forEach { entry ->
                handleEntry(entry, jarFile, handler)
            }
        }
    }

    private fun alwaysChecked(resolvedArtifact: ResolvedArtifact): Boolean {
        if (resolvedArtifacts.contains(resolvedArtifact.id.displayName)) {
            return true
        }
        resolvedArtifacts.add(resolvedArtifact.id.displayName)
        return false
    }

    private val emptyByteArray = ByteArray(0)

    private fun handleEntry(jarEntry: JarEntry,
                            jarFile: JarFile,
                            handler: (JarEntry, ByteArray) -> Unit) {

        if (jarEntry.isDirectory) {
            handler.invoke(jarEntry, emptyByteArray)
            return
        }

        try {
            jarFile.getInputStream(jarEntry).use { inputStream ->
                val os = ByteArrayOutputStream()
                val buffer = ByteArray(0xFFFF)
                var len: Int = inputStream.read(buffer)
                while (len != -1) {
                    os.write(buffer, 0, len)
                    len = inputStream.read(buffer)
                }
                os.flush()
                handler.invoke(jarEntry, os.toByteArray())
            }
        } catch (e: IOException) {
            throw GradleUsageException(CLASS_ENTRY_ERROR, e)
        }
    }

}
