package com.openmodloader.loader.parse;

import com.github.zafarkhaja.semver.Version;
import com.openmodloader.api.mod.ModMetadata;
import com.openmodloader.api.mod.config.IModConfigurator;
import com.openmodloader.loader.ModConstructionException;
import com.openmodloader.loader.ModConstructor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

// TODO: Handle all fields in here
@XmlRootElement(name = "mod")
public class ModDeclaration {
    @XmlAttribute
    private String id;
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String version = "1.0.0";

    @XmlAttribute
    private boolean global;

    @XmlElement
    private Configurator configurator;

    @XmlElement
    private Depends depends = new Depends();

    @XmlElement(name = "children")
    private Children children = new Children();

    public static ModDeclaration parse(InputStream input) throws ParseException {
        try {
            JAXBContext context = JAXBContext.newInstance(ModDeclaration.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            ModDeclaration declaration = (ModDeclaration) unmarshaller.unmarshal(input);
            validate(declaration);

            return declaration;
        } catch (JAXBException e) {
            throw new ParseException("Malformed XML", e);
        }
    }

    private static void validate(ModDeclaration declaration) throws ParseException {
        if (declaration.id == null) {
            throw new ParseException("Id must be specified in mod declaration!");
        }

        if (declaration.configurator == null || declaration.configurator.path == null) {
            throw new ParseException("Mod configurator was not specified for '" + declaration.id + "'!");
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name != null ? name : id;
    }

    public String getVersion() {
        return version;
    }

    public String getConfigurator() {
        return configurator.path;
    }

    public String getAdapter() {
        return configurator.adapter;
    }

    public ModMetadata buildMetadata() {
        return new ModMetadata(id, Version.valueOf(version));
    }

    public Collection<ModDeclaration> getChildren() {
        return Collections.unmodifiableCollection(children.mods);
    }

    @SuppressWarnings("unchecked")
    public IModConfigurator constructConfigurator(ModConstructor constructor) throws ModConstructionException {
        try {
            Class<?> configuratorClass = Class.forName(configurator.path);
            if (!IModConfigurator.class.isAssignableFrom(configuratorClass)) {
                throw new ModConstructionException(configurator.path + " was not of type IModConfigurator");
            }
            return constructor.constructConfigurator((Class<IModConfigurator>) configuratorClass, configurator.adapter);
        } catch (ClassNotFoundException e) {
            throw new ModConstructionException(e);
        }
    }

    public boolean isGlobal() {
        return global;
    }

    private static class Configurator {
        @XmlAttribute(name = "class")
        private String path;
        @XmlAttribute(name = "adapter")
        private String adapter = "java";
    }

    private static class Depends {
        @XmlElement(name = "permission")
        private List<PermissionDepend> permissions = new ArrayList<>();

        @XmlElement(name = "mod")
        private List<ModDepend> mods = new ArrayList<>();
    }

    private static class PermissionDepend {
        @XmlAttribute
        private String id;
        @XmlAttribute
        private boolean required = true;
    }

    private static class ModDepend {
        @XmlAttribute
        private String id;
        @XmlAttribute
        private String version = "*";
        @XmlAttribute
        private boolean required = true;
    }

    private static class Children {
        @XmlElement(name = "mod")
        private List<ModDeclaration> mods = new ArrayList<>();
    }
}
