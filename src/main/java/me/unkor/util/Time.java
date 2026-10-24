package me.unkor.util;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Time {
    public static float getTime() {
        return (float)glfwGetTime();
    }
}
