package me.unkor.scene;

import me.unkor.Window;
import me.unkor.listeners.KeyListener;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {
    private boolean changingScene = false;
    private float timeToChangingScene = 2.0f;


    public LevelEditorScene() {

    }

    @Override
    public void update(float dt) {
        if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = true;
        }

        if (changingScene && timeToChangingScene > 0) {
            timeToChangingScene -= dt;
            Window.get().r -= dt * 5.0f;
            Window.get().g -= dt * 5.0f;
            Window.get().b -= dt * 5.0f;
        } else if (changingScene) {
            Window.changeScene(1);
        }
    }
}
