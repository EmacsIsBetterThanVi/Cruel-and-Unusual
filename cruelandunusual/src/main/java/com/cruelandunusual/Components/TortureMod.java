package com.cruelandunusual.Components;

import com.cruelandunusual.API.ModLoader;
import org.json.JSONObject;

import java.awt.event.ActionListener;

public abstract class TortureMod {
    private final ActionListener[] keyCallbacks;
    private final UnusualCreature[] creatureTypes;
    private final Shape[] shapes;
    private final Material[] materials;
    private boolean initilized;
    private final ModLoader modLoader;
    private final Class title;
    private final String packagePath;
    private final String packageName;
    public TortureMod(ModLoader ml, JSONObject info, String packagePath, String packageName) {
        modLoader = ml;
        this.packagePath = packagePath;
        this.packageName = packageName;
        title = ml.loadTitle(this);
        keyCallbacks = new ActionListener[info.getJSONArray("keyCallbacks").length()];
        creatureTypes = new UnusualCreature[info.getJSONArray("creatureTypes").length()];
        materials = new Material[info.getJSONArray("materials").length()];
        shapes = new Shape[info.getJSONArray("shapes").length()];
    }
    public final String getPath(){
        return packagePath;
    }
    public final String getPackage(){
        return packageName;
    }
    public final ActionListener[] getKeyCallbacks() {
        return keyCallbacks;
    }

    public final UnusualCreature[] getCreatureTypes() {
        return creatureTypes;
    }

    public final Shape[] getShapes() {
        return shapes;
    }

    public final Material[] getMaterials() {
        return materials;
    }
    public final CruelScreen getTitle(){
        try {
            return ((CruelScreen) title.getDeclaredConstructors()[0].newInstance());
        } catch (Exception ignored){
            return null;
        }
    }
    public final void init() {
        if (initilized) return;
        modLoader.registerMod(this);
        try {
            initilized=true;
            load();
        } catch (Exception ignored){
            modLoader.unloadMod(this);
        }
    }
    public abstract void load() throws Exception;
    public abstract void unload();

    public final ModLoader getModLoader() {
        return modLoader;
    }
}
