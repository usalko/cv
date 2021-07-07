package com.usalko.gradle.plugin.asm

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

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Opcodes

internal class UsageAnnotationVisitor(private val visitorsContext: VisitorsContext) :
        AnnotationVisitor(Opcodes.ASM7) {

    override fun visit(name: String?, value: Any) {
        super.visit(name, value)
        visitorsContext.acceptClass(name)
    }
}
