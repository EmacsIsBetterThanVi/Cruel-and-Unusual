package com.cruelandunusual.Components;

import com.cruelandunusual.API.MOUSE;
import com.cruelandunusual.CruelAndUnusual;
import com.cruelandunusual.Screen;

import java.io.IOException;

import static com.cruelandunusual.API.PALLET.handlePallet;

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
        if (MOUSE.inRect(font.getRectC(360, 310, "Single Player"))){
            font.writeC(360, 310, "Single Player", handlePallet(255, 0, 0));
            if (MOUSE.isMouseDown(MOUSE.LEFT_MOUSE)) CruelAndUnusual.screen = Screen.WORLD_SELECT_INIT;
        }
        else font.writeC(360, 310, "Single Player", 256);
        if (MOUSE.inRect(font.getRectC(360, 350, "Multi Player"))) {
            font.writeC(360, 350, "Multi Player", handlePallet(255, 0, 0));
            if (MOUSE.isMouseDown(MOUSE.LEFT_MOUSE)) CruelAndUnusual.screen = Screen.MULTI_PLAYER_INIT;
        }
        else font.writeC(360, 350, "Multi Player", 256);
        if (MOUSE.inRect(font.getRectC(360, 390, "Options"))) {
            font.writeC(360, 390, "Options", handlePallet(255, 0, 0));
            if (MOUSE.isMouseDown(MOUSE.LEFT_MOUSE)) CruelAndUnusual.screen = Screen.OPTIONS_INIT;
        }
        else font.writeC(360, 390, "Options", 256);
        if (MOUSE.inRect(font.getRectC(360, 430, "Exit"))){
            font.writeC(360, 430, "Exit", handlePallet(255, 0, 0));
            if (MOUSE.isMouseDown(MOUSE.LEFT_MOUSE)) CruelAndUnusual.shutdown();
        }
        else font.writeC(360, 430, "Exit", 256);
    }
    public void shutdown(){
    }
}