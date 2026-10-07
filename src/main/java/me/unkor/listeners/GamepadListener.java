package me.unkor.listeners;

public class GamepadListener {
    private static GamepadListener instance;

    private GamepadListener() {

    }

    public static GamepadListener get() {
        if (instance == null)
            instance = new GamepadListener();
        return instance;
    }

    public static void joystickCallback(long window) {

    }
}
