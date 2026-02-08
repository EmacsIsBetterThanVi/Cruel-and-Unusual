package com.cruelandunusual.Components;

import com.cruelandunusual.CruelAndUnusual;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class UnusualFont {
    public byte[][] buffer;
    private final int gh;
    private final int gw;
    public UnusualFont(URL resource) throws IOException {
        BufferedImage img = ImageIO.read(resource);
        int h = img.getHeight();
        int w = img.getWidth();
        gh = h/8;
        gw = w/16;
        buffer = new byte[256][gh*gw];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int c = img.getRGB(x, y);
                buffer[(y/gh)*16+(x/gw)][(y%gh)*gw+(x%gw)] = (c % 0xFFFFFF) == 0 ? (byte)0 : (byte)1;
            }
        }
    }

    public int getGlyphHight() {
        return gh;
    }

    public int getGlyphWidth() {
        return gw;
    }
    public void write(int x, int y, String text, int color){
        for (int i = 0; i < text.length(); i++) {
            putChar(x+(gw*i), y, text.charAt(i), color);
        }
    }
    protected void putChar(int xp, int yp, int glyph, int color){
        if (glyph>255 || glyph<0) glyph = 0;
        for (int y = 0; y < gh; y++) {
            for (int x = 0; x < gw; x++) {
                if (buffer[glyph][y * gw + x]==1) CruelAndUnusual.frameBuffer[((yp + y) * 720) + (xp + x)] = color;
            }
        }
    }
}
