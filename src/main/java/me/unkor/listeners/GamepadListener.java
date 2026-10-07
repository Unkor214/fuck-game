package me.unkor.listeners;

/*
* TODO:
*  1. Обрытный вызов для оброботки сигналов джостика
*/

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
