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

import org.objectweb.asm.*

class UsageFieldVisitor internal constructor(private val visitorsContext: VisitorsContext) : FieldVisitor(Opcodes.ASM7) {

    override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor {
        return UsageAnnotationVisitor(visitorsContext)
    }

    override fun visitTypeAnnotation(typeRef: Int, typePath: TypePath?, descriptor: String,
                                     visible: Boolean): AnnotationVisitor {
        return UsageAnnotationVisitor(visitorsContext)
    }

    override fun visitAttribute(attribute: Attribute) {
        super.visitAttribute(attribute)
        visitorsContext.acceptClass(attribute.type)
    }

}
