package com.cruelandunusual;
import com.cruelandunusual.Components.*;
import com.cruelandunusual.FrontEnds.*;

import java.awt.*;

public final class CruelAndUnusual {
    // CONSTANTS
    public static final int KEY_UP = 0;
    public static final int KEY_DOWN = 3;
    public static final int KEY_JUST_DOWN = 2;
    public static final int KEY_JUST_UP = 1;
    public static final int major = 0;
    public static final int minor = 1;
    public static final int revision = 0;
    // VARIABLES
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
    // API METHODS
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
    // Pixel API(Only use the pixel API for things which are fine running very slowly, otherwise, use images)
    public static void drawPixel(int x, int y, int palletID){
        backCanvas[x+(y*720)] = palletID;
    }
    public static void drawPixel(int x, int y, float red, float green, float blue){
        drawPixel(x, y, handlePallet(red, green, blue));
    }
    public static void drawPixel(int x, int y, int red, int green, int blue){
        drawPixel(x, y, handlePallet(red, green, blue));
    }
    // Pallet API
    public static int handlePallet(float rf, float gf, float bf) {
        return handlePallet(rf, gf, bf, backPallet);
    }
    public static int handlePallet(int red, int green, int blue){
        float rf = red/255f, gf = green/255f, bf = blue/255f;
        return handlePallet(rf, gf, bf, backPallet);
    }
    public static int handlePallet(int red, int green, int blue, float[] p){
        float rf = red/255f, gf = green/255f, bf = blue/255f;
        return handlePallet(rf, gf, bf, p);
    }
    public static int handlePallet(float rf, float gf, float bf, float[] p){
        if (rf==0 && gf == 0 && bf == 0) return (p.length/3)-1;
        for (int i = 0; i < (p.length/3)-1; i++) {
            if (p[i*3]==rf && p[i*3+1]==gf && p[i*3+2]==bf) return i;
            else if (p[i*3]==0 && p[i*3+1]==0 && p[i*3+2]==0) {
                p[i*3]=rf;
                p[i*3+1]=gf;
                p[i*3+2]=bf;
                return i;
            }
        }
        return (p.length/3)-1; // Pallet error, always displays as black, use this for true black
    }
    // END API SECTION
    public static void run(){
        // It is possible for a keystroke to miss its update, so we check both the previous and the frame before
        switch (screen){
            case TITLE_INIT:
                resetFrame();
                SCREEN = new TitleMenu(SCREEN);
                screen = Screen.TITLE;
                break;
            case TITLE:
                SCREEN.run();
                break;
        }
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
        int front=1; // Front end 0 = headless, 1 = awt, 2 = lwjgl
        keys = new int[256];
        screen = Screen.TITLE_INIT;
        for (int i=0; i<args.length; i++){
            switch (args[i]){
                case "version":
                    System.out.println(major+"."+minor+"."+revision);
                    System.exit(0);
                case "headless":
                case "frontend=none":
                    front=0;
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
        if (front == 1) frontEnd = new CruelAndUnusualAwt();
        if (front == 0) frontEnd = new CruelAndUnusualConsole();
        ShutdownHook sh = new ShutdownHook();
        Runtime.getRuntime().addShutdownHook(sh);
        System.out.println("INITIALIZING FRONT END: "+frontEnd.toString());
        frontEnd.INIT();
        frontEnd.run();
        System.exit(0);
    }
}
class ShutdownHook extends Thread{
    @Override
    public void run() {
        System.out.println("SHUTTING DOWN FRONT END: "+CruelAndUnusual.frontEnd.toString());
        CruelAndUnusual.frontEnd.exit();
        Runtime.getRuntime().halt(0);
    }
}