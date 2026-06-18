package com.EmacsIsBetterThanVi.CruelAndUnusual.Components;

import com.EmacsIsBetterThanVi.CruelAndUnusual.API.ModLoader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final JSONObject info;
    public TortureMod(ModLoader ml, JSONObject info, String packagePath, String packageName) {
        Class titleClass;
        modLoader = ml;
        this.packagePath = packagePath;
        this.packageName = packageName;
        try {
            titleClass = ml.loadTitle(this);
        } catch (Exception e){
            titleClass = null;
        }
        title = titleClass;
        if (info.optJSONArray("keyCallbacks")!=null) keyCallbacks = new ActionListener[info.getJSONArray("keyCallbacks").length()];
        else keyCallbacks = new ActionListener[0];
        if (info.optJSONArray("creatureTypes")!=null) creatureTypes = new UnusualCreature[info.getJSONArray("creatureTypes").length()];
        else creatureTypes = new UnusualCreature[0];
        if (info.optJSONArray("materials")!=null) materials = new Material[info.getJSONArray("materials").length()];
        else materials = new Material[0];
        if (info.optJSONArray("shapes")!=null) shapes = new Shape[info.getJSONArray("shapes").length()];
        else shapes = new Shape[0];
        this.info = info;
    }
    public final String getPath(){
        return packagePath;
    }
    public final String getPackage(){
        return packageName;
    }
    public final String getDisplayName() {return info.getString("modName");}
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
    public final CruelScreen getTitle(CruelScreen SCREEN) throws Exception {
        return ((CruelScreen) title.getDeclaredConstructors()[0].newInstance(SCREEN));
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

    public final List<JSONObject> getOptions(){
        JSONArray jsa= info.optJSONArray("options");
       List<JSONObject> ls = new ArrayList<>();
        if (jsa==null) return ls;
        for (int i=0; i<jsa.length(); i++) {
            ls.add(jsa.getJSONObject(i));
        }
        return ls;
    };
}
