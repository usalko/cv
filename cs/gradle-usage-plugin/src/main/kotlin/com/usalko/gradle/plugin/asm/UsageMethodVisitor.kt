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

internal class UsageMethodVisitor(private val visitorsContext: VisitorsContext) : MethodVisitor(Opcodes.ASM7) {

    override fun visitMethodInsn(opcode: Int, owner: String, name: String, desc: String, itf: Boolean) {
        super.visitMethodInsn(opcode, owner, name, desc, itf)
        visitorsContext.acceptClass(owner)
    }

    override fun visitAnnotationDefault(): AnnotationVisitor {
        return UsageAnnotationVisitor(visitorsContext)
    }

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

    override fun visitTypeInsn(opcode: Int, type: String?) {
        super.visitTypeInsn(opcode, type)
        visitorsContext.acceptClass(type)
    }

    override fun visitFieldInsn(opcode: Int, owner: String, name: String, descriptor: String) {
        super.visitFieldInsn(opcode, owner, name, descriptor)
        visitorsContext.acceptClass(owner)
    }

    override fun visitInvokeDynamicInsn(name: String, descriptor: String, bootstrapMethodHandle: Handle,
                                        vararg bootstrapMethodArguments: Any) {
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle,
                *bootstrapMethodArguments)
        visitorsContext.acceptClass(descriptor)
    }

    override fun visitMultiANewArrayInsn(descriptor: String, numDimensions: Int) {
        super.visitMultiANewArrayInsn(descriptor, numDimensions)
        visitorsContext.acceptClass(descriptor)
    }

    override fun visitInsnAnnotation(typeRef: Int, typePath: TypePath?, descriptor: String,
                                     visible: Boolean): AnnotationVisitor {
        return UsageAnnotationVisitor(visitorsContext)
    }

    override fun visitTryCatchBlock(start: Label, end: Label, handler: Label, type: String?) {
        super.visitTryCatchBlock(start, end, handler, type)
        visitorsContext.acceptClass(type)
    }

    override fun visitTryCatchAnnotation(typeRef: Int, typePath: TypePath?,
                                         descriptor: String, visible: Boolean): AnnotationVisitor {
        return UsageAnnotationVisitor(visitorsContext)
    }

    override fun visitLocalVariable(name: String, descriptor: String, signature: String?, start: Label,
                                    end: Label, index: Int) {
        super.visitLocalVariable(name, descriptor, signature, start, end, index)
        visitorsContext.acceptClass(descriptor)
    }

    override fun visitLocalVariableAnnotation(typeRef: Int, typePath: TypePath?,
                                              start: Array<Label>, end: Array<Label>,
                                              index: IntArray, descriptor: String,
                                              visible: Boolean): AnnotationVisitor {
        return UsageAnnotationVisitor(visitorsContext)
    }

}
