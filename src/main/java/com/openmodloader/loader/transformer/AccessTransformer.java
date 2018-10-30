/*
 * Copyright 2016 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.openmodloader.loader.transformer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import javax.annotation.Nonnull;
import java.lang.reflect.Modifier;

public class AccessTransformer {

    @Nonnull
    public ClassNode transform(ClassNode classNode) {
        if (!Modifier.isPublic(classNode.access)) {
            classNode.access = publify(classNode.access);
        }

        for (MethodNode method : classNode.methods) {
            if (!Modifier.isPublic(method.access)) {
                method.access = publify(method.access);
            }
        }

        for (FieldNode field : classNode.fields) {
            if (!Modifier.isPublic(field.access)) {
                field.access = publify(field.access);
            }
        }

        return classNode;
    }

    private static int publify(int access) {
        return access & ~Opcodes.ACC_PRIVATE & ~Opcodes.ACC_PROTECTED | Opcodes.ACC_PUBLIC;
    }
}
