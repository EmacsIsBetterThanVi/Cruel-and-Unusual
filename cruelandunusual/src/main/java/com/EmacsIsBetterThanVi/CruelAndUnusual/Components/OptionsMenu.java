package com.EmacsIsBetterThanVi.CruelAndUnusual.Components;

import com.EmacsIsBetterThanVi.CruelAndUnusual.API.MOUSE;
import com.EmacsIsBetterThanVi.CruelAndUnusual.CruelAndUnusual;
import com.EmacsIsBetterThanVi.CruelAndUnusual.Screen;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

import static com.EmacsIsBetterThanVi.CruelAndUnusual.API.PALLET.handlePallet;
import static com.EmacsIsBetterThanVi.CruelAndUnusual.CruelAndUnusual.config;
import static com.EmacsIsBetterThanVi.CruelAndUnusual.CruelAndUnusual.options;

public class OptionsMenu implements CruelScreen{
    UnusualFont font;
    TortureMod tab;
    public OptionsMenu(CruelScreen cs) {
        try {
            cs.shutdown();
        } catch (Exception ignored) {
        }// Deletes the old screen
        try {
            font = new UnusualFont(this.getClass().getClassLoader().getResource("Title/default.png"));
            tab = CruelAndUnusual.systemModLoader.getMods()[0];
        } catch (IOException ignored) {
            System.out.println("Could not find resource");
            System.exit(1);
        }
    }
    public void render(JSONObject opt){

    }
    public String asString(Object obj, String type, String atype){
        if (obj==null) return "";
        switch (type){
            case "int":
                return ((Integer) obj).toString();
            case "array":
                StringBuilder s = new StringBuilder();
                for (Object a: (Object[]) obj) {
                    s.append(asString(obj, atype));
                    s.append(", ");
                }
                return s.toString();
            case "bool":
                return ((Boolean) obj).toString();
            case "float":
                return ((Float) obj).toString();
            default:
                return obj.toString();
        }
    }
    public String asString(Object obj, String type){
        return asString(obj, type, null);
    }
    public Object fromString(String obj, String type){
        return fromString(obj, type, null);
    }
    public Object fromString(String obj, String type, String atype){
        switch (type){
            case "float":
                return Float.parseFloat(obj);
            case "array":
                String[] str = obj.split(", ");
                Object[] arr = new Object[str.length];
                for (int i = 0; i < str.length; i++) {
                    arr[i] = fromString(str[i], atype);
                }
                return arr;
            case "int":
                return Integer.parseInt(obj);
            case "bool":
                return Boolean.parseBoolean(obj);
            default:
                return obj;
        }
    }
    public Object getValue(JSONObject opt){
        if (config.query(opt.getString("key"))!=null) return config.query(opt.getString("key"));
        else return opt.optString("default", "");
    }
    public void setValue(JSONObject opt, Object value){
        try {
            ((JSONObject) config.query(opt.getString("key").substring(0, config.getString("key").lastIndexOf('/'))))
                    .put(opt.getString("key").substring(config.getString("key").lastIndexOf('/')), value);
        } catch (Exception ignored) {
            config.put(opt.getString("key").substring(opt.getString("key").lastIndexOf('/')+1), value);
        }
    }
    int scroll = 0;
    @Override
    public void run() {
        CruelAndUnusual.resetFrame();
        font.write(0, 0, "Back", 256, 1);
        if (MOUSE.inRect(font.getRect(0, 0, "Back"))){
            font.write(0, 0, "Back", handlePallet(255, 0, 0));
            if (MOUSE.isMouseJustDown(MOUSE.LEFT_MOUSE)) {
                try {
                    FileWriter fw = new FileWriter(System.getProperty("user.home") + "/.emacsisbetterthanvi/cruelandunusual/config");
                    fw.write(config.toString());
                    fw.close();
                } catch (Exception ignored) {}
                CruelAndUnusual.frontEnd.configChanged();
                CruelAndUnusual.screen = Screen.TITLE_INIT;
            }
        }
        int x=0;
        for (TortureMod tm: options.keySet()){
            if (tm!=tab) {
                if (MOUSE.inRect(font.getRect(20 + (x % 400), 16 + Math.floorDiv(x, 400) * 20, tm.getDisplayName()))) {
                    font.write(20 + (x % 400), 16 + Math.floorDiv(x, 400) * 20, tm.getDisplayName(), handlePallet(255, 0, 0));
                    if (MOUSE.isMouseJustDown(MOUSE.LEFT_MOUSE)) {
                        tab=tm;
                        scroll=0;
                    }
                } else
                    font.write(20 + (x % 400), 16 + Math.floorDiv(x, 400) * 20, tm.getDisplayName(), 256, 1);
            } else {
                font.write(20 + (x % 400), 16 + Math.floorDiv(x, 400) * 20, tm.getDisplayName(), handlePallet(255, 0, 255), 1);
                int y=0;
                for (JSONObject opt:options.get(tm)) {
                    if(y>=scroll) {
                        font.write(20, 45 + (y-scroll) * 20, opt.getString("name"), handlePallet(0, 0, 255), 1);
                        font.write(260, 45 + (y-scroll) * 20, asString(getValue(opt), opt.getString("TYPE"), opt.optJSONObject("values", new JSONObject()).optString("TYPE")), handlePallet(0, 0, 255), 1);
                        if (MOUSE.inRect(font.getRect(260, 45 + (y-scroll) * 20, asString(getValue(opt), opt.getString("TYPE"), opt.optJSONObject("values", new JSONObject()).optString("TYPE"))))){
                            switch (opt.getString("TYPE")){
                                case "bool":
                                    if(MOUSE.isMouseJustDown(MOUSE.LEFT_MOUSE)){
                                        setValue(opt, !(Boolean) getValue(opt));
                                    }
                                    break;
                            }
                        }
                    }
                    y++;
                }
            }
            x+=80;
        }
    }

    @Override
    public void shutdown() {

    }
}
