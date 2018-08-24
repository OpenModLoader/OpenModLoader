package com.openmodloader.loader.launch;

import com.openmodloader.loader.transformer.AccessTransformer;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

import java.nio.file.Path;

//This makes everything public
public class OMLPluginService implements ILaunchPluginService {

    AccessTransformer transformer = new AccessTransformer();

    @Override
    public String name() {
        return "omlaccestransformer";
    }

    @Override
    public void addResource(Path resource, String name) {
    }

    @Override
    public ClassNode processClass(ClassNode classNode, Type classType) {
        //Makes everything public at runtime :D
        return transformer.transform(classNode);
    }

    @Override
    public <T> T getExtension() {
        return null;
    }

    @Override
    public boolean handlesClass(Type classType, boolean isEmpty) {
        //Everything in the MC package, and not in a package
        if (classType.getClassName().startsWith("net.minecraft") || !classType.getClassName().contains(".")) {
            return !isEmpty;
        }
        return false;
    }
}
