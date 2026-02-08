package com.cruelandunusual.FrontEnds;

import com.cruelandunusual.CruelAndUnusual;
import com.cruelandunusual.Screen;

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
        w.setLayout(new BorderLayout());
        w.add(px, BorderLayout.CENTER);
        CruelAndUnusual.resetFrame();
        px.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton()>3 || e.getButton()<1) return;
                registerKeyEvent(e.getButton()+252, KEY_JUST_DOWN);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton()>3 || e.getButton()<1) return;
                registerKeyEvent(e.getButton()+252, KEY_JUST_UP);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        px.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {}
            @Override
            public void mouseMoved(MouseEvent e) {
                if(px.scale==0) return;
                mouseX = (e.getX()-((px.getWidth()/2)-360*px.scale))/px.scale;
                mouseY = (e.getY()-((px.getHeight()/2)-300*px.scale))/px.scale;
            }
        });
        w.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    switch (screen){
                        case TITLE:
                            shouldClose = true;
                            break;
                        case OPTIONS:
                        case WORLD_OPTIONS:
                            screen = Screen.TITLE_INIT;
                            break;
                        case RULES:
                            screen = Screen.WORLD_OPTIONS_INIT;
                            break;
                        case GAME:
                            menu = !menu;
                            break;
                    }
                } else {
                    registerKeyEvent(e.getKeyCode(), KEY_JUST_DOWN);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                registerKeyEvent(e.getKeyCode(), KEY_JUST_UP);
            }
        });
        timer1 = new Timer(1, e -> px.repaint(1));
        timer1.start();
    }

    @Override
    public void exit() {
        timer1.stop();
        w.dispose();
    }
}