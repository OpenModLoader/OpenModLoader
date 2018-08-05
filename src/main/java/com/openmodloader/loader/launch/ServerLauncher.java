package com.openmodloader.loader.launch;

import cpw.mods.modlauncher.Launcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//Used when starting the server from the jar file.
public class ServerLauncher {

    public static void main(String[] args) {
        List<String> argList = new ArrayList<>();
        Collections.addAll(argList, args);
	    argList.add("--launchTarget");
	    argList.add("oml");

        Object[] objectList = argList.toArray();
        String[] stringArray = Arrays.copyOf(objectList, objectList.length, String[].class);
	    Launcher.main(stringArray);
    }

}
