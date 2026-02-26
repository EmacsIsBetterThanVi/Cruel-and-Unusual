package com.cruelandunusual.API;

import com.cruelandunusual.Components.*;
import org.json.JSONObject;

import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Scanner;

public final class ModLoader {
    private ArrayList<TortureMod> mods;
    private ArrayList<ActionListener> keyCallbacks;
    private ArrayList<UnusualCreature> creatures;
    private ArrayList<UnusualCreature> creatureTypes;
    private ArrayList<Shape> shapes;
    private ArrayList<Material> materials;
    public final String modBasePath;

    public ModLoader(String modBasePath) {
        this.modBasePath = modBasePath;
    }

    public void registerMod(TortureMod mod, int priority){
        mods.remove(mod);
        mods.add(priority, mod);
    }

    public void registerMod(TortureMod mod){
        mods.remove(mod);
        mods.add(mod);
    }
    public Class loadTitle(TortureMod mod){
        return loadClass(mod.getPath(), mod.getPackage());
    }
    private Class loadClass(String ModPath, String className){
        try {
            File file = new File(ModPath);
            if (!file.exists()) throw new RuntimeException("Could not locate folder");
            URL url = file.toURI().toURL();
            URLClassLoader loader = new URLClassLoader(new URL[]{url});
            return loader.loadClass(className);
        } catch (MalformedURLException e) {
            throw new RuntimeException("URL format error");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found");
        }
    }
    public void unloadMod(TortureMod mod){
        mod.unload();
        for (ActionListener a: mod.getKeyCallbacks()){
            removeKeyCallback(a);
        }
        for (UnusualCreature c: mod.getCreatureTypes()){
            removeCreatureType(c);
        }
        for (Shape s: mod.getShapes()){
            removeShape(s);
        }
        for (Material m: mod.getMaterials()){
            removeMaterial(m);
        }
    }

    public void removeMaterial(Material m) {
        materials.remove(m);
    }

    public void removeShape(Shape s) {
        shapes.remove(s);
    }

    public void removeCreatureType(UnusualCreature c) {
        creatureTypes.remove(c);
    }

    public void removeKeyCallback(ActionListener a) {
        keyCallbacks.remove(a);
    }

    public void registerKeyCallback(int KEYCODE, int ks, ActionListener action){

    }
    public int registerCreatureType(UnusualCreature creature){
        creatureTypes.remove(creature);
        creatureTypes.add(creature);
        return creatureTypes.indexOf(creature);
    }
    public boolean loadMod(String name){
        try {
            Scanner fr = new Scanner(new File(modBasePath + name + "/" + "module.json"));
            StringBuilder s = new StringBuilder();
            while (fr.hasNextLine()) {
                s.append(fr.nextLine());
            }
            fr.close();
            JSONObject jsonObject = new JSONObject(s.toString());
            TortureMod tm = ((TortureMod) loadClass(modBasePath + name, name + "." + name).getDeclaredConstructors()[0].newInstance(this, jsonObject, modBasePath + name + "/", name));
            if (tm != null) {
                tm.init();
                return true;
            }
        } catch (Exception ignored){}
        return false;
    }
}
