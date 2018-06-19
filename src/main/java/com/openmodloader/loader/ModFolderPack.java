package com.openmodloader.loader;

import com.google.common.collect.Lists;
import net.minecraft.resource.ResourceNotFoundException;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.pack.PhysicalResourcePack;
import net.minecraft.util.Identifier;
import net.minecraft.util.IdentifierException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class ModFolderPack extends PhysicalResourcePack {
    private final String modDomain;

    public ModFolderPack(File aFile1, String modDomain) {
        super(aFile1);
        this.modDomain = modDomain;
    }

    @Override
    protected InputStream openByPath(String path) throws IOException {
        File v1 = new File(this.location, path);
        if (v1.exists() && v1.isFile()) {
            return new FileInputStream(v1);
        }
        throw new ResourceNotFoundException(this.location, path);
    }

    @Override
    protected boolean canHandleByPath(String path) {
        return new File(this.location, path).exists();
    }

    @Override
    public Collection<Identifier> listFiles(ResourceType type, String path, int i, Predicate<String> predicate) {
        File v1 = new File(this.location, type.getFolder());
        List<Identifier> v2 = Lists.newArrayList();
        this.a(new File(new File(v1, modDomain), path), i, modDomain, v2, path + "/", predicate);
        return v2;
    }

    private void a(File aFile1, int aInteger2, String aString3, List<Identifier> aList4, String aString5, Predicate<String> aPredicate6) {
        File[] v1 = aFile1.listFiles();
        if (v1 != null) {
            File[] var8 = v1;
            int var9 = v1.length;

            for (int var10 = 0; var10 < var9; ++var10) {
                File v5 = var8[var10];
                if (v5.isDirectory()) {
                    if (aInteger2 > 0) {
                        this.a(v5, aInteger2 - 1, aString3, aList4, aString5 + v5.getName() + "/", aPredicate6);
                    }
                } else if (!v5.getName().endsWith(".mcmeta") && aPredicate6.test(v5.getName())) {
                    try {
                        aList4.add(new Identifier(aString3, aString5 + v5.getName()));
                    } catch (IdentifierException var13) {
                        var13.printStackTrace();
                    }
                }
            }
        }

    }

    @Override
    public Set<String> getResourceDomains(ResourceType type) {
        return Collections.singleton(modDomain);
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public String getName() {
        ModInfo info = OpenModLoader.getModInfo(modDomain);
        return info == null ? modDomain : info.getName() == null ? modDomain : info.getName();
    }
}
