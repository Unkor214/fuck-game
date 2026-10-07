package me.unkor.listeners;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    private static KeyListener instance;

    //350 всез привязок для lwjgl
    private boolean keyPressed[] = new boolean[350];

    private KeyListener() {

    }

    public static KeyListener get() {
        if (instance == null)
            instance = new KeyListener();

        return instance;
    }

    //функция обратного вызова, для клавиатуры
    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        //если кнопка нажата, то [текушяя привязки клавиши] = нажата
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
        }
        //но а тут на оборот
        else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        return get().keyPressed[keyCode];
    }
}
