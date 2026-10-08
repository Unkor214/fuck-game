package me.unkor.scene;

import me.unkor.Window;
import me.unkor.listeners.KeyListener;

import java.awt.event.KeyEvent;

//редактирование сцены
public class LevelEditorScene extends Scene {
    private int changingScene = 0;
    private float timeToChangingScene = 2.0f;


    public LevelEditorScene() {

    }

    @Override
    public void update(float dt) {
        if (changingScene == 0 && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = 1;
        } else if (changingScene == 0 && KeyListener.isKeyPressed(KeyEvent.VK_B)) {
            changingScene = 2;
        }

        if (changingScene == 2 && timeToChangingScene > 0) {
            timeToChangingScene -= dt;
            Window.get().r -= dt * 0;
            Window.get().g -= dt * 0;
            Window.get().b -= dt * 10.0f;
        } else if (changingScene == 2 && timeToChangingScene > 0) {
            Window.changeScene(1);
        }

        if (changingScene == 1 && timeToChangingScene > 0) {
            timeToChangingScene -= dt;
            Window.get().r -= dt * 5.0f;
            Window.get().g -= dt * 5.0f;
            Window.get().b -= dt * 5.0f;
        } else if (changingScene == 1) {
            Window.changeScene(1);
        }
    }
}
