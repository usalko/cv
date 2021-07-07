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

class UsageClassVisitor(private val visitorsContext: VisitorsContext) : ClassVisitor(Opcodes.ASM7) {

    override fun visit(version: Int, access: Int, name: String, signature: String?,
                       superName: String?, interfaces: Array<String>) {
        super.visit(version, access, name, signature, superName, interfaces)
        visitorsContext.setCurrentClassName(name)
    }

    override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor {
        return UsageAnnotationVisitor(visitorsContext)
    }

    override fun visitTypeAnnotation(typeRef: Int, typePath: TypePath, descriptor: String,
                                     visible: Boolean): AnnotationVisitor {
        return UsageAnnotationVisitor(visitorsContext)
    }

    override fun visitAttribute(attribute: Attribute) {
        super.visitAttribute(attribute)
        visitorsContext.acceptClass(attribute.type)
    }

    override fun visitNestMember(nestMember: String) {
        super.visitNestMember(nestMember)
        visitorsContext.acceptClass(nestMember)
    }

    override fun visitInnerClass(name: String, outerName: String?, innerName: String?, access: Int) {
        super.visitInnerClass(name, outerName, innerName, access)
        visitorsContext.acceptClass(innerName)
    }

    override fun visitField(access: Int, name: String, descriptor: String, signature: String?,
                            value: Any?): FieldVisitor {
        return UsageFieldVisitor(visitorsContext)
    }

    override fun visitMethod(access: Int, name: String, descriptor: String, signature: String?,
                             exceptions: Array<String>?): MethodVisitor {
        return UsageMethodVisitor(visitorsContext)
    }

}
