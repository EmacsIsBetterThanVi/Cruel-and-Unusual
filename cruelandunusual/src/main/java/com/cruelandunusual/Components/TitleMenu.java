package com.cruelandunusual.Components;

import com.cruelandunusual.CruelAndUnusual;

import java.io.IOException;

public class TitleMenu implements CruelScreen{
    public CruelImage MorkBorgLogo;
    public UnusualFont font;
    public <T> TitleMenu(CruelScreen cs){
        try {cs.shutdown();} catch (Exception ignored){}// Deletes the old screen
        try {
            MorkBorgLogo = new CruelImage(this.getClass().getClassLoader().getResource("Title/Icon.png"));
            MorkBorgLogo.blit(60, 200);
            font = new UnusualFont(this.getClass().getClassLoader().getResource("Title/default.png"));
            font.write(0, 0, "test", CruelAndUnusual.handlePallet(255, 255, 255));
        } catch (IOException ignored) {}
    }

    public void run(){

    }
    public void shutdown(){
    }
}