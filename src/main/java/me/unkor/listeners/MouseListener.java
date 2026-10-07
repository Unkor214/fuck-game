package me.unkor.listeners;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {
    private static MouseListener instance;

    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY;

    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;

    private MouseListener() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    public static MouseListener get() {
        if (instance == null) {
            instance = new MouseListener();
        }

        return instance;
    }

    //Эвент перемешение мыши
    public static void mousePosCallback(long window, double xpos, double ypos) {
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xpos;
        get().yPos = ypos;
        //проверка перетаскивоние
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1]
                || get().mouseButtonPressed[2];
    }

    //Эвент нажатых клавиш мыши
    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        //если кнопка нажата
        //то ввод кнопки в массиве истиный
        if (action == GLFW_PRESS)
            //проверка на количество кнопк мыши
            if (button < get().mouseButtonPressed.length)
                get().mouseButtonPressed[button] = true;
        //если кнопка не нажато
        //то ввод кнопки в массиве равен ложе и зажим тоже
        else if (action == GLFW_RELEASE) {
            //проверка на количество кнопк мыши
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
    }

    //Эвент прокретки мыши
    public static void scrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    //Сброс кадров
    public static void endFrame() {
        get().scrollX = 0.0;
        get().scrollY = 0.0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX() {
        return (float)get().xPos;
    }

    public static float getY() {
        return (float)get().yPos;
    }

    public static float getDx() {
        return (float)(get().lastX - get().xPos);
    }

    public static float getDy() {
        return (float)(get().lastY - get().yPos);
    }

    public static float getScrollX() {
        return (float)get().scrollX;
    }

    public static float getScrollY() {
        return (float)get().scrollY;
    }

    public static boolean isDagging() {
        return isDagging();
    }

    public static boolean mouseButtonDown(int button) {
        if (button < get().mouseButtonPressed.length)
            return get().mouseButtonPressed[button];
        else
            return false;
    }
}
