package me.unkor.util;

public class Time {
    //время начало
    public static float timeStarted = System.nanoTime();

    //функция получение текушего времени
    public static float getTime() {
        //текушее время
        return (float)((System.nanoTime() - timeStarted) * 1E-9);
    }
}
