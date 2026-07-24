package com.EmacsIsBetterThanVi.CruelAndUnusual.FrontEnds;

import com.EmacsIsBetterThanVi.CruelAndUnusual.CruelAndUnusual;
import com.EmacsIsBetterThanVi.CruelAndUnusual.Screen;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


import static com.EmacsIsBetterThanVi.CruelAndUnusual.CruelAndUnusual.*;

public class CruelAndUnusualAwt implements CruelAndUnusualFrontEnd {
    public PixelCanvasAwt px;
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
    public void configChanged(){
        System.out.println(config);
        System.out.println(oldConfig);
        if (config.getBoolean("Fullscreen")!=oldConfig.getBoolean("Fullscreen")) {
            w.dispose();
            INIT();
        }
    }
    @Override
    public void INIT() {
        w= new Frame("Cruel and Unusual");
        w.setBackground(new Color(0.9f, 0.9f, 0.0f));
        w.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {
                shutdown();
            }
            @Override
            public void windowClosed(WindowEvent e) {}

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        w.setResizable(false);
        if (config.getBoolean("Fullscreen")) {
            w.setUndecorated(true);
            w.setExtendedState(Frame.MAXIMIZED_BOTH);
        } else {
            w.setResizable(true);
            w.setSize(720, 650);
        }
        w.setVisible(true);
        px = new PixelCanvasAwt();
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
                        case WORLD_SELECT:
                        case MULTI_PLAYER:
                            mp = false;
                            screen = Screen.TITLE_INIT;
                            break;
                        case CHARACTER_SELECT:
                        case WORLD_OPTIONS:
                            if (mp) screen = Screen.MULTI_PLAYER_INIT;
                            else screen = Screen.WORLD_SELECT_INIT;
                            break;
                        case CHARACTER_OPTIONS:
                            screen = Screen.CHARACTER_SELECT_INIT;
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

    @Override
    public String prompt(String p) {
        AwtPrompt awtPrompt = new AwtPrompt(p);
        //noinspection StatementWithEmptyBody
        while (!awtPrompt.done);
        awtPrompt.dispose();
        return awtPrompt.tf.getText();
    }

    @Override
    public String prompt(String p, boolean secret) {
        AwtPrompt awtPrompt = new AwtPrompt(p, secret);
        //noinspection StatementWithEmptyBody
        while (!awtPrompt.done);
        awtPrompt.dispose();
        return awtPrompt.tf.getText();
    }
}
class AwtPrompt {
    public Frame f;
    public TextField tf;
    public volatile boolean done;
    public JPasswordField pw;
    public AwtPrompt(String p){
        f = new Frame(p);
        f.add(new Label(p));
        done=false;
        tf = new TextField(20);
        tf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                done=true;
            }
        });
        f.add(tf);
        f.setSize(400, 100);
        f.setVisible(true);
        f.setAutoRequestFocus(true);
    }
    public AwtPrompt(String p, boolean secret){
        f = new Frame(p);
        f.add(new Label(p));
        done=false;
        pw = new JPasswordField(20);
        tf = new TextField();
        pw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tf.setText(new String(pw.getPassword()));
                done=true;
            }
        });
        f.add(pw);
        f.setSize(400, 100);
        f.setVisible(true);
        f.setAutoRequestFocus(true);
    }
    public void dispose(){
        f.dispose();
    }
}