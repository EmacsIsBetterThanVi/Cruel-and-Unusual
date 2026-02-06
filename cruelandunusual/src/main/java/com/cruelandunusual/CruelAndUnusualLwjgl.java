package com.cruelandunusual;

import java.nio.*;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.system.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class CruelAndUnusualLwjgl implements CruelAndUnusualFrontEnd{
    public long window;
//    public boolean menu=false;
//    public ByteBuffer windowBuffer;
    public GLCapabilities glCapabilities;
    @Override
    public void INIT() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Could not initialize GLFW");
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(720, 600, "Cruel and Unusual", glfwGetPrimaryMonitor(), NULL);
        if (window == NULL) throw new RuntimeException("Could not create window");
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) CruelAndUnusual.menu = !CruelAndUnusual.menu;
        });
//        windowBuffer = ByteBuffer.allocate(720*600*3);
//        windowBuffer.order(ByteOrder.nativeOrder());
//        for (int i=0; i<720*600*3; i++){
//            windowBuffer.put((byte)0);
//        }
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            glfwGetWindowSize(window, pWidth, pHeight);
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2);
        }
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }
    @Override
    public void run(){
        glCapabilities= GL.createCapabilities();
        GLUtil.setupDebugMessageCallback();
        glClearColor(0.9f, 0.9f, 0.0f, 0.0f);
//        drawPixel(100, 100, 0xAA, 0xBB, 0xCC);
        /*for (int i=0; i<720*600*3; i++){
            if (windowBuffer.get(i)!=0)
                System.out.print(Integer.toHexString(windowBuffer.get(i)).substring(6, 8)+":"+Integer.toHexString(i)+",");
        }*/
        System.out.println(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());
        //draw();
        while (!glfwWindowShouldClose(window)){
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glfwSwapBuffers(window);
            glfwPollEvents();
            switch (CruelAndUnusual.screen) {
                case 0:
                {
                    if (CruelAndUnusual.menu) glfwSetWindowShouldClose(window, true);
                }
                break;
            }
        }
    }
    @Override
    public String toString(){
        return "LWJGL";
    }
    @Override
    public void exit(){
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
//    public void drawPixel(int y, int x, int red, int green, int blue){
//        if (x>=720) return;
//        if (y>=600) return;
//        windowBuffer.put(3*(y*720+x), (byte)red);
//        windowBuffer.put(3*(y*720+x)+1, (byte)green);
//        windowBuffer.put(3*(y*720+x)+2, (byte)blue);
//    }
//    public void draw() {
//        int texture = glGenTextures();
//        glBindTexture(GL_TEXTURE_2D, texture);
//        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, 720, 600, 0, GL_RGB, GL_UNSIGNED_BYTE, windowBuffer);
//        glBindTexture(GL_TEXTURE_2D, 0);
//        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, texture);
//    }
}
