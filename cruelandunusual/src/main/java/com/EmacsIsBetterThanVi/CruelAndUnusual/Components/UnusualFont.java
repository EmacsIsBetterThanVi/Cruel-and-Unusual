package com.EmacsIsBetterThanVi.CruelAndUnusual.Components;

import com.EmacsIsBetterThanVi.CruelAndUnusual.CruelAndUnusual;

import javax.imageio.ImageIO;
import java.awt.*;
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
        gh = h/16;
        gw = w/16;
        buffer = new byte[256][gh*gw];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int c = img.getRGB(x, y);
                buffer[(y/gh)*16+(x/gw)][(y%gh)*gw+(x%gw)] = (c & 0xFFFFFF) == 0 ? (byte)0 : (byte)1;
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
            putChar(x+(gw*i), y, text.charAt(i), color, 1);
        }
    }
    public void writeC(int x, int y, String text, int color){
        x = x-(text.length()/2)*gw;
        y = y - gh/2;
        for (int i = 0; i < text.length(); i++) {
            putChar(x+(gw*i), y, text.charAt(i), color,1);
        }
    }
    public void write(int x, int y, String text, int color, int scale){
        for (int i = 0; i < text.length(); i++) {
            putChar(x+(gw*i*scale), y, text.charAt(i), color,scale);
        }
    }
    public Rectangle getRectC(int x, int y, String text){
        return new Rectangle(x-(text.length()*gw)/2, y - gh/2, text.length()*gw, gh);
    }
    public Rectangle getRect(int x, int y, String text){
        return new Rectangle(x, y, text.length()*gw, gh);
    }
    private void putChar(int xp, int yp, int glyph, int color, int scale){
        if (glyph>255 || glyph<0) glyph = 0;
        for (int y = 0; y < gh; y++) {
            for (int j = 0; j < scale; j++) {
                for (int x = 0; x < gw; x++) {
                    for (int i = 0; i < scale; i++) {
                        if (buffer[glyph][y * gw + x] == 1)
                            CruelAndUnusual.backCanvas[((yp + (y*scale)+j) * 720) + (xp + (x*scale) + i)] = color;
                    }
                }
            }
        }
    }

}
