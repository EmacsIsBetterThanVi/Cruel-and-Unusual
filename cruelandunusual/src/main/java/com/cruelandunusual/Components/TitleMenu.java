package com.cruelandunusual.Components;

import com.cruelandunusual.API.MOUSE;
import com.cruelandunusual.CruelAndUnusual;

import java.io.IOException;

public class TitleMenu implements CruelScreen{
    public CruelImage MorkBorgLogo;
    public UnusualFont font;
    public <T> TitleMenu(CruelScreen cs){
        try {cs.shutdown();} catch (Exception ignored){}// Deletes the old screen
        try {
            MorkBorgLogo = new CruelImage(this.getClass().getClassLoader().getResource("Title/Icon.png"));
            font = new UnusualFont(this.getClass().getClassLoader().getResource("Title/default.png"));
        } catch (IOException ignored) {
            System.out.println("Could not find resource");
            System.exit(1);
        }
    }

    public void run(){
        CruelAndUnusual.resetFrame();
        MorkBorgLogo.blit(60, 100);
        font.write(20, 10, "Cruel and Unusual", 256, 5);
        if (MOUSE.inRect(font.getRectC(360, 310, "Single Player"))) font.writeC(360, 310, "Single Player", CruelAndUnusual.handlePallet(255, 0, 0));
        else font.writeC(360, 310, "Single Player", 256);
        if (MOUSE.inRect(font.getRectC(360, 350, "Multi Player"))) font.writeC(360, 350, "Multi Player", CruelAndUnusual.handlePallet(255, 0, 0));
        else font.writeC(360, 350, "Multi Player", 256);
        if (MOUSE.inRect(font.getRectC(360, 390, "Options"))) font.writeC(360, 390, "Options", CruelAndUnusual.handlePallet(255, 0, 0));
        else font.writeC(360, 390, "Options", 256);
        if (MOUSE.inRect(font.getRectC(360, 430, "Exit"))){
            font.writeC(360, 430, "Exit", CruelAndUnusual.handlePallet(255, 0, 0));
            if (MOUSE.isMouseDown(MOUSE.LEFT_MOUSE)) System.exit(0);
        }
        else font.writeC(360, 430, "Exit", 256);
    }
    public void shutdown(){
    }
}