package com.usalko.maven.plugin.usage.asm;

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

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;

class UsageMethodVisitor extends MethodVisitor {

    private final VisitorsContext visitorsContext;

    UsageMethodVisitor(VisitorsContext visitorsContext) {
        super(Opcodes.ASM7);
        this.visitorsContext = visitorsContext;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        super.visitMethodInsn(opcode, owner, name, desc, itf);
        visitorsContext.acceptClass(owner);
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        return new UsageAnnotationVisitor(visitorsContext);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return new UsageAnnotationVisitor(visitorsContext);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor,
            boolean visible) {
        return new UsageAnnotationVisitor(visitorsContext);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        super.visitAttribute(attribute);
        visitorsContext.acceptClass(attribute.type);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        super.visitTypeInsn(opcode, type);
        visitorsContext.acceptClass(type);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        super.visitFieldInsn(opcode, owner, name, descriptor);
        visitorsContext.acceptClass(owner);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle,
            Object... bootstrapMethodArguments) {
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle,
                bootstrapMethodArguments);
        visitorsContext.acceptClass(descriptor);
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        super.visitMultiANewArrayInsn(descriptor, numDimensions);
        visitorsContext.acceptClass(descriptor);
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor,
            boolean visible) {
        return new UsageAnnotationVisitor(visitorsContext);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        super.visitTryCatchBlock(start, end, handler, type);
        visitorsContext.acceptClass(type);
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath,
            String descriptor, boolean visible) {
        return new UsageAnnotationVisitor(visitorsContext);
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start,
            Label end, int index) {
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
        visitorsContext.acceptClass(descriptor);
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath,
            Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        return new UsageAnnotationVisitor(visitorsContext);
    }

}
