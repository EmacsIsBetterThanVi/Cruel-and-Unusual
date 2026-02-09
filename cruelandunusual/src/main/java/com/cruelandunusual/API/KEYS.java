package com.cruelandunusual.API;

import static com.cruelandunusual.CruelAndUnusual.*;

/**
 * KEYS is inteanded to function as an Enum for key code constants.
 */
public final class KEYS {
    public static final int ESCAPE = 27;
    public static final int RETURN = 10;
    public static final int BACKSPACE = 8;
    public static boolean isKeyDown(int KEYCODE){
        return getKeyState(KEYCODE)==KEY_DOWN || getKeyState(KEYCODE)==KEY_JUST_DOWN;
    }
    public static boolean isKeyUP(int KEYCODE){
        return getKeyState(KEYCODE)==KEY_UP || getKeyState(KEYCODE)==KEY_JUST_UP;
    }
    public static boolean isKeyJustDown(int KEYCODE){
        return getKeyState(KEYCODE)==KEY_JUST_DOWN;
    }
    public static boolean isKeyJustUp(int KEYCODE){
        return getKeyState(KEYCODE)==KEY_JUST_UP;
    }
    public static int getKeyState(int KEYCODE){
        try {return ((keys[KEYCODE] >> 8) & 3); }
        catch (Exception ignored) {return 0;}
    }
}
