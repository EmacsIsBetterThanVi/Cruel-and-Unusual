package com.EmacsIsBetterThanVi.CruelAndUnusual.API;

import java.awt.*;

import static com.EmacsIsBetterThanVi.CruelAndUnusual.CruelAndUnusual.*;
import static com.EmacsIsBetterThanVi.CruelAndUnusual.CruelAndUnusual.KEY_JUST_UP;
import static com.EmacsIsBetterThanVi.CruelAndUnusual.CruelAndUnusual.keys;

public final class MOUSE {
    public static final int LEFT_MOUSE = 1;
    public static final int RIGHT_MOUSE = 2;
    public static final int CENTER_MOUSE = 3;
    public static boolean isMouseDown(int MOUSE_BUTTON){
        return getMouseState(MOUSE_BUTTON)==KEY_DOWN;
    }
    public static boolean isMouseUP(int MOUSE_BUTTON){
        return getMouseState(MOUSE_BUTTON)==KEY_UP;
    }
    public static boolean isMouseJustDown(int MOUSE_BUTTON){
        return getMouseState(MOUSE_BUTTON)==KEY_JUST_DOWN;
    }
    public static boolean isMouseJustUp(int MOUSE_BUTTON){
        return getMouseState(MOUSE_BUTTON)==KEY_JUST_UP;
    }
    public static boolean inRect(Rectangle r){
        return (mouseX>=r.x) && (mouseX<=r.x+r.width) && (mouseY>=r.y) && (mouseY<=r.y+r.height);
    }
    public static int getMouseState(int MOUSE_BUTTON){
        try { return ((keys[MOUSE_BUTTON+252] >> 8) & 3); }
        catch (Exception ignored) {return 0;}
    }
}
