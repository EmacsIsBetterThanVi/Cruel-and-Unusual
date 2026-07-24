package com.EmacsIsBetterThanVi.CruelAndUnusual;
import com.EmacsIsBetterThanVi.CruelAndUnusual.API.ModLoader;
import com.EmacsIsBetterThanVi.CruelAndUnusual.Components.CruelScreen;
import com.EmacsIsBetterThanVi.CruelAndUnusual.Components.TitleMenu;
import com.EmacsIsBetterThanVi.CruelAndUnusual.Components.TortureMod;
import com.EmacsIsBetterThanVi.CruelAndUnusual.Components.UnusualCreature;
import com.EmacsIsBetterThanVi.CruelAndUnusual.FrontEnds.CruelAndUnusualAwt;
import com.EmacsIsBetterThanVi.CruelAndUnusual.FrontEnds.CruelAndUnusualConsole;
import com.EmacsIsBetterThanVi.CruelAndUnusual.FrontEnds.CruelAndUnusualFrontEnd;
import com.EmacsIsBetterThanVi.CruelAndUnusual.FrontEnds.CruelAndUnusualLwjgl;
import com.EmacsIsBetterThanVi.CruelAndUnusual.Components.*;
import com.EmacsIsBetterThanVi.CruelAndUnusual.FrontEnds.*;
import com.EmacsIsBetterThanVi.AccountLib;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static com.EmacsIsBetterThanVi.CruelAndUnusual.API.PALLET.handlePallet;

public final class CruelAndUnusual {
    // CONSTANTS
    public static final int KEY_UP = 0;
    public static final int KEY_DOWN = 3;
    public static final int KEY_JUST_DOWN = 2;
    public static final int KEY_JUST_UP = 1;
    public static final int major = 0;
    public static final int minor = 3;
    public static final int revision = 0;
    // VARIABLES
    public static String UUID_NAME;
    public static boolean mp=false;
    public static boolean menu=false;
    public static int[] frameBuffer;
    public static float[] pallet;
    public static int[] backCanvas;
    public static float[] backPallet;
    public static int[] keys; // 00000STT - S=3 DOWN, S=0 UP, S=1 JUST UP, S=2 JUST DOWN. T is the FRAME
    public static Screen screen;
    public static CruelAndUnusualFrontEnd frontEnd;
    public static int FRAME;
    public static CruelScreen SCREEN;
    public static int mouseX=0;
    public static int mouseY=0;
    public static JSONObject config;
    public static Map<TortureMod, List<JSONObject>> options; // <Mod, List<Option>>
    public static ModLoader systemModLoader;
    public static JSONObject oldConfig;

    // API METHODS
    public static UnusualCreature createCreature(UnusualCreature type){
        try {
            return (UnusualCreature) (type.getClass().getConstructors()[0].newInstance());
        } catch (Exception ignored){
            return null;
        }
    }

    // INPUT API
    // Injects a key event into the buffer. Use KEY_UP, KEY_DOWN, KEY_JUST_DOWN, and KEY_JUST_UP for ks
    public static void registerKeyEvent(int KEYCODE, int ks){
        if (FRAME<60)
            keys[KEYCODE] = (ks << 8) | FRAME;
        else keys[KEYCODE] = (ks << 8);
    }
    // Updates the screen
    public static void resetFrame(){
        frameBuffer=backCanvas;
        pallet=backPallet;
        backCanvas = new int[720*600];
        backPallet = new float[257*3];
        backPallet[0] = 0.9f;
        backPallet[1] = 0.9f;
    }
    // Pixel API(Only use the pixel API for things which are fine running very slowly, otherwise, use images, This is mostly here to expose an underlying interface which is simpler than the array)
    public static void drawPixel(int x, int y, int palletID){
        backCanvas[x+(y*720)] = palletID;
    }
    public static void drawPixel(int x, int y, float red, float green, float blue){
        drawPixel(x, y, handlePallet(red, green, blue));
    }
    public static void drawPixel(int x, int y, int red, int green, int blue){
        drawPixel(x, y, handlePallet(red, green, blue));
    }
    // END API SECTION
    public static void run(){
        switch (screen){
            case TITLE_INIT:
                resetFrame();
                boolean titleCreated = false;
                for(TortureMod tm: systemModLoader.getMods()){
                    try {
                        SCREEN = tm.getTitle(SCREEN);
                        screen = Screen.TITLE;
                        titleCreated=true;
                        break;
                    } catch (Exception ignored) {}
                }
                if (!titleCreated) {
                    SCREEN = new TitleMenu(SCREEN);
                    screen = Screen.TITLE;
                }
                break;
            case OPTIONS_INIT:
                resetFrame();
                options = new HashMap<>();
                for (TortureMod tm: systemModLoader.getMods()){
                    try {
                        options.put(tm, tm.getOptions());
                    } catch (Exception ignored) {}
                }
                SCREEN = new OptionsMenu(SCREEN);
                screen = Screen.OPTIONS;
                break;
            default:
                SCREEN.run();
                break;
        }
        // It is possible for a keystroke to miss its update, so we check both the previous and the frame before
        for (int i = 0; i < 256; i++) {
            if (keys[i] == ((KEY_JUST_DOWN << 8) | (FRAME-1)) || keys[i] == ((KEY_JUST_DOWN << 8) | (FRAME-2))){
                keys[i] = (KEY_DOWN << 8) | FRAME;
            }
            if (keys[i] == ((KEY_JUST_UP << 8) | (FRAME-1)) || keys[i] == ((KEY_JUST_UP << 8) | (FRAME-2))){
                keys[i] = (KEY_UP << 8) | FRAME;
            }
        }
        FRAME++;
        if (FRAME>=60) FRAME=0;
    }
    public static void main(String[] args) {
        int front =0; // Front end 0 = headless, 1 = awt, 2 = lwjgl
        keys = new int[256];
        screen = Screen.TITLE_INIT;
        for (int i=0; i<args.length; i++){
            switch (args[i]){
                case "version":
                    System.out.println(major+"."+minor+"."+revision);
                    System.exit(0);
                case "headless":
                case "frontend=none":
                case "frontend=cli":
                    front =3;
                    break;
                case "frontend=awt":
                    front = 1;
                    break;
                case "frontend=lwjgl":
                    front = 2;
                    break;
            }
        }

        if (front == 2) frontEnd = new CruelAndUnusualLwjgl();
        if (front == 1 || front == 0) frontEnd = new CruelAndUnusualAwt();
        // AWT is supported no matter what, so we use it for the default font end until the prefered one can be loaded
        if (front == 3) frontEnd = new CruelAndUnusualConsole();
        ShutdownHook shutdownHook = new ShutdownHook();
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        resetFrame();
        resetFrame();
        File f = new File(System.getProperty("user.home")+"/.emacsisbetterthanvi/cruelandunusual/");
        if (!f.exists()){
            try {
                UUID_NAME = frontEnd.prompt("Username");
                //noinspection ResultOfMethodCallIgnored
                f.mkdirs();
                FileWriter fw = new FileWriter(f.getAbsolutePath()+"/config");
                config = new JSONObject();
                config.put("UUID_NAME", UUID_NAME);
                buildConfig();
                fw.write(config.toString());
                fw.close();
            } catch (IOException ignored){
                System.err.println("Could not create config");
                System.exit(1);
            }
        } else {
            try {
                Scanner fr = new Scanner(new File(f.getAbsolutePath()+"/config"));
                StringBuilder s = new StringBuilder();
                while (fr.hasNextLine()){
                    s.append(fr.nextLine());
                }
                fr.close();
                config = new JSONObject(s.toString());
                UUID_NAME = config.getString("UUID_NAME");
                if (config.getInt("major")<major || config.getInt("minor")<minor){
                    FileWriter fw = new FileWriter(f.getAbsolutePath()+"/config");
                    buildConfig();
                    fw.write(config.toString());
                    fw.close();
                }
                if (front == 0){ // Only load preferred front end if a specific one was not provided
                    front = config.getInt("PreferedFrontEnd");
                    if (front == 2) frontEnd = new CruelAndUnusualLwjgl();
                    if (front == 1) frontEnd = new CruelAndUnusualAwt();
                    if (front == 3) frontEnd = new CruelAndUnusualConsole();
                }
            } catch (Exception ignored){
                config = new JSONObject();
                UUID_NAME = frontEnd.prompt("Username");
                config.put("UUID_NAME", UUID_NAME);
                buildConfig();
            }
        }
        oldConfig=new JSONObject(config.toString());
        if (config.getBoolean("UseAccountLib")) {
            try {
                AccountLib.connect();
                String password = frontEnd.prompt("Password", true);
                AccountLib.login(UUID_NAME, password);
            } catch(Throwable t) {
                for(; t != null; t = t.getCause()) {
                    System.err.println(t);
                    for(StackTraceElement e: t.getStackTrace())
                        System.err.println("\tat "+e);
                }
                config.put("UseAccountLib", false);
            }
        }
        System.out.println("INITIALIZING FRONT END: "+frontEnd.toString());
        frontEnd.INIT();
        System.out.println("Performing Initial Mod Loading");
        f = new File(System.getProperty("user.home")+"/.emacsisbetterthanvi/cruelandunusual/mods");
        if (!f.exists()) f.mkdir();
        systemModLoader = new ModLoader(f.getAbsolutePath());
        try {
            Scanner fr = new Scanner(systemModLoader.getClass().getClassLoader().getResourceAsStream("module.json"));
            StringBuilder s = new StringBuilder();
            while (fr.hasNextLine()){
                s.append(fr.nextLine());
            }
            fr.close();
            JSONObject obj = new JSONObject(s.toString());
            TortureMod tm = new jsonTortureMod(systemModLoader, obj, "", "");
            tm.init();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            System.err.println("Could not initialize core module");
            System.exit(1);
        }
        for (String s : f.list()) {
            System.out.println(s);
        }
        frontEnd.run();
        System.exit(0);
    }
    public static void shutdown(){
        System.out.println("SHUTTING DOWN FRONT END: "+ CruelAndUnusual.frontEnd.toString());
        CruelAndUnusual.frontEnd.exit();
        Runtime.getRuntime().halt(0);
    }
    public static void buildConfig(){
        config.put("major", major);
        config.put("minor", minor);
        config.putOnce("PreferedFrontEnd", "1");
        config.putOnce("Fullscreen", true);
        config.putOnce("UseAccountLib", false);
    }
}
class ShutdownHook extends Thread{
    @Override
    public void run() {
        CruelAndUnusual.shutdown();
    }
}
