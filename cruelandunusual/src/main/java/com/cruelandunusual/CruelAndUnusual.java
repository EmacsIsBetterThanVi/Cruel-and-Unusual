package com.cruelandunusual;
public class CruelAndUnusual {
    public static boolean menu=false;
    public static byte[] frameBuffer;
    public static float[] pallet;
    public static int[] keys; // 000TTSKK - K is the key. S=1 DOWN, S=0 UP, T is the FRAME
    public static int screen; // 0 - Title, 1 - game, 2 - world options, 3 - rules, 4 - options
    public static CruelAndUnusualFrontEnd frontEnd;
    public static int MouseX;
    public static int MouseY;
    public static int FRAME;
    public static void run(){
        FRAME++;
        if (FRAME>=60) FRAME=0;
    }
    public static void main(String[] args) {
        int front=1; // Front end 0 = headless, 1 = awt, 2 = lwjgl
        for (int i=0; i<args.length; i++){
            switch (args[i]){
                case "version":
                    System.out.println("0.0.0");
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