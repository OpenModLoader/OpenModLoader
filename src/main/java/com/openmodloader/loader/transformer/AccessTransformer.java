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

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class AccessTransformer implements ITransformer<ClassNode> {

	//TODO we need to write a tool to auto gen these for each POMF version
	private String[] targets = new String[] {
		"net.minecraft.util.math.shapes.ShapeUtils",
		"cdz",
		"cdw",
		"net.minecraft.util.math.shapes.VoxelShapePart",
		"net.minecraft.util.math.shapes.VoxelShape",
		"net.minecraft.util.math.DoubleScale",
		"cea",
		"ced",
		"cee",
		"net.minecraft.client.particle.config.ParticleConfigDefault",
		"dec",
		"net.minecraft.NativeImage",
		"net.minecraft.NativeImage$b",
		"net.minecraft.client.texture.TextureUtil",
		"net.minecraft.client.render.TextureAtlasSprite",
		"cuk",
		"net.minecraft.render.debug.RenderDebugNeighborUpdate",
		"net.minecraft.client.gui.GuiScreen"
	};

	@Nonnull
	@Override
	public ClassNode transform(ClassNode classNode, ITransformerVotingContext context) {
		boolean isClassPublic = (classNode.access & Opcodes.ACC_PUBLIC) != 0;
		if (!isClassPublic) {
			classNode.access &= ~Opcodes.ACC_PRIVATE;
			classNode.access &= ~Opcodes.ACC_PROTECTED;
			classNode.access |= Opcodes.ACC_PUBLIC;
		}

		for (MethodNode method : classNode.methods) {
			boolean isPublic = (method.access & Opcodes.ACC_PUBLIC) != 0;
			if (!isPublic) {
				method.access &= ~Opcodes.ACC_PRIVATE;
				method.access &= ~Opcodes.ACC_PROTECTED;
				method.access |= Opcodes.ACC_PUBLIC;
			}
		}
		for (FieldNode field : classNode.fields) {
			boolean isPublic = (field.access & Opcodes.ACC_PUBLIC) != 0;
			if (!isPublic) {
				field.access &= ~Opcodes.ACC_PRIVATE;
				field.access &= ~Opcodes.ACC_PROTECTED;
				field.access |= Opcodes.ACC_PUBLIC;
			}
		}
		return classNode;
	}

	@Nonnull
	@Override
	public TransformerVoteResult castVote(ITransformerVotingContext context) {
		return TransformerVoteResult.YES;
	}

	@Nonnull
	@Override
	public Set<Target> targets() {
		return Arrays.stream(targets).map(Target::targetClass).collect(Collectors.toSet());
	}
}