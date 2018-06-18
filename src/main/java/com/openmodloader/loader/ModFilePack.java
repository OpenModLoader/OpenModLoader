package com.openmodloader.loader;

import com.google.common.collect.Lists;
import net.minecraft.resource.ResourceNotFoundException;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.pack.PhysicalResourcePack;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ModFilePack extends PhysicalResourcePack {
    private ZipFile zipFile;
    private String modDomain;

    public ModFilePack(File aFile1, String domain) {
        super(aFile1);
        this.modDomain = domain;
        try {
            zipFile = new ZipFile(aFile1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected InputStream openByPath(String path) throws IOException {
        ZipEntry entry = zipFile.getEntry(path);
        if (entry == null)
            throw new ResourceNotFoundException(this.location, path);
        return zipFile.getInputStream(entry);
    }

    @Override
    protected boolean canHandleByPath(String path) {
        return zipFile.getEntry(path) != null;
    }

    @Override
    public Collection<Identifier> listFiles(ResourceType type, String path, int i, Predicate<String> predicate) {
        List<Identifier> files = Lists.newArrayList();
        String basePath = type.getFolder() + "/";
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (!entry.isDirectory() && entry.getName().startsWith(basePath)) {
                String fileName = entry.getName().substring(basePath.length());
                int afterPath = fileName.indexOf('/');
                if (afterPath > 0) {
                    String v8 = fileName.substring(0, afterPath);
                    String v9 = fileName.substring(afterPath + 1);
                    if (v9.startsWith(path + "/")) {
                        String[] v10 = v9.substring(path.length() + 2).split("/");
                        if (v10.length >= i + 1 && !fileName.endsWith(".mcmeta") && predicate.test(v9)) {
                            files.add(new Identifier(v8, v9));
                        }
                    }
                }
            }
        }
        return files;
    }

    @Override
    public Set<String> getResourceDomains(ResourceType type) {
        return Collections.singleton(modDomain);
    }

    @Override
    public void close() throws IOException {
        IOUtils.closeQuietly(zipFile);
    }
}
