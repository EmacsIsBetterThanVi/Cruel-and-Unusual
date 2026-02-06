package com.cruelandunusual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


import static com.cruelandunusual.CruelAndUnusual.*;

public class CruelAndUnusualAwt implements CruelAndUnusualFrontEnd {
    public PixelCanvas px;
    public Frame w;
    public Timer timer1;
    public volatile boolean shouldClose;
    @Override
    public String toString(){
        return "AWT";
    }
    @Override
    public void run() {
        while (!shouldClose) {
            CruelAndUnusual.run();
        }
    }

    @Override
    public void INIT() {
        w= new Frame("Cruel and Unusual");
        w.setBackground(new Color(0.9f, 0.9f, 0.0f));
        w.setUndecorated(true);
        w.setExtendedState(Frame.MAXIMIZED_BOTH);
        w.setVisible(true);
        w.setResizable(false);
        px = new PixelCanvas();
        frameBuffer = new byte[720*600];
        pallet = new float[256*3];
        w.setLayout(new BorderLayout());
        w.add(px, BorderLayout.CENTER);
        pallet[0] = 0.9f;
        pallet[1] = 0.9f;
        pallet[2] = 0.9f;
        w.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        w.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (screen){
                    case 0:
                    {
                        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                            shouldClose = true;
                        } else System.out.println(e.getKeyCode());
                    }
                    break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        timer1 = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                px.repaint();
            }
        });
        timer1.start();
    }

    @Override
    public void exit() {
        timer1.stop();
        w.dispose();
    }
}
class PixelCanvas extends Canvas {
    public Image im;
    public Graphics g2;
    public PixelCanvas(){
        super();

    }
    @Override
    public void update(Graphics g){
        int scale = (int) Math.min(Math.floor(getWidth()/720d), Math.floor(getHeight()/600d));
        if (scale==0) return;
        if (g2==null) {
            im = createImage(720*scale, 600*scale);
            g2=im.getGraphics();
        }
        g.drawImage(im, (getWidth()/2)-360*scale, (getHeight()/2)-300*scale, this);
        for (int i = 0; i < 720*600; i++) {
            g2.setColor(new Color(pallet[3*frameBuffer[i]], pallet[3*frameBuffer[i]+1], pallet[3*frameBuffer[i]+2]));
            g2.fillRect(scale*(i%720), (int)Math.floor(i/720d)*scale, scale, scale);
        }
    }
}