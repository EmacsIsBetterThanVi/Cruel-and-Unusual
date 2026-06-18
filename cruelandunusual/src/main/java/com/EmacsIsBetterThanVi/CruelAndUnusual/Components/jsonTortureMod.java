package com.EmacsIsBetterThanVi.CruelAndUnusual.Components;

import com.EmacsIsBetterThanVi.CruelAndUnusual.API.ModLoader;
import org.json.JSONObject;

public class jsonTortureMod extends TortureMod {
    public jsonTortureMod(ModLoader ml, JSONObject info, String packagePath, String packageName) {
        super(ml, info, packagePath, packageName);
    }

    @Override
    public void load() throws Exception {
    }

    @Override
    public void unload() {

    }
}
