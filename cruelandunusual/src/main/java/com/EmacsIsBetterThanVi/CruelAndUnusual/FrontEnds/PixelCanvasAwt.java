package com.EmacsIsBetterThanVi.CruelAndUnusual.FrontEnds;

import java.awt.*;

import static com.EmacsIsBetterThanVi.CruelAndUnusual.CruelAndUnusual.frameBuffer;
import static com.EmacsIsBetterThanVi.CruelAndUnusual.CruelAndUnusual.pallet;

public class PixelCanvasAwt extends Canvas {
    public Image im;
    public Graphics g2;
    public int scale;
    public PixelCanvasAwt(){
        super();
    }
    @Override
    public void update(Graphics g){
        scale = (int) Math.min(Math.floor(getWidth()/720d), Math.floor(getHeight()/600d));
        if (scale==0) return;
        if (g2==null) {
            im = createImage(720*scale, 600*scale);
            g2=im.getGraphics();
        }
        setBackground(new Color(pallet[0], pallet[1], pallet[2]));
        //setBackground(Color.BLACK);
        g.drawImage(im, (getWidth()/2)-360*scale, (getHeight()/2)-300*scale, this);
        for (int i = 0; i < 720*600; i++) {
            g2.setColor(new Color(pallet[3*frameBuffer[i]], pallet[3*frameBuffer[i]+1], pallet[3*frameBuffer[i]+2]));
            g2.fillRect(scale*(i%720), (int)Math.floor(i/720d)*scale, scale, scale);
        }
    }
}
