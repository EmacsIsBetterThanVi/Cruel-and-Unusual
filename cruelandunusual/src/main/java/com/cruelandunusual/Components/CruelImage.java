package com.cruelandunusual.Components;

import com.cruelandunusual.CruelAndUnusual;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class CruelImage {
    public int[] buffer;
    public float[] pallet;
    private boolean surfacep;
    private final int h;
    private final int w;
    private int background;
    public CruelImage(URL resource) throws IOException {
        background=17;
        surfacep=false;
        BufferedImage img = ImageIO.read(resource);
        h = img.getHeight();
        w = img.getWidth();
        buffer = new int[w*h];
        pallet=new float[17*3];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int c = img.getRGB(x, y);
                buffer[y * w + x] = CruelAndUnusual.handlePallet((c >> 16) & 0xFF, (c >> 8) & 0xFF, c & 0xFF, pallet);
            }
        }
    }
    public boolean isSurface() {
        return surfacep;
    }
    public int getWidth() {
        return w;
    }
    public int getHeight() {
        return h;
    }
    public void setBackground(float red, float green, float blue) {
        this.background = CruelAndUnusual.handlePallet(red, green, blue, pallet);
    }
    public void setBackground(int background) {
        this.background = background;
    }
    public int getBackground() {
        return background;
    }
    public Rectangle getRect(int x, int y){
        return new Rectangle(x, y, w, h);
    }
    // Makes the image into a surface.
    public void makeSurface(){
        surfacep=true;
        pallet=new float[6];
        for (int i = 0; i < h*w; i++) {
            buffer[i] = (buffer[i] == 0) ? 0 : 1;
        }
    }
    // Renders the image on the screen
    public void blit(int xp, int yp){
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int id = buffer[y * w + x];
                if (id != background)
                    CruelAndUnusual.backCanvas[((yp + y) * 720) + (xp + x)] =
                            CruelAndUnusual.handlePallet(pallet[id*3], pallet[id*3+1], pallet[id*3+2]);
            }
        }
    }
    // Renders an image through a shape. This should be called on the shape, then pass the shape the texture
    public void blit(int x, int y, CruelImage texture){
        if (!surfacep){
            throw new IllegalStateException("Can not blit a texture onto a non-surface Image");
        }
    }
}
