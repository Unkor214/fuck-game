package me.unkor.scene;

/*
* TODO:
*  1. оброботка физичиский процессов
*/

import me.unkor.Camera;
import me.unkor.fuck.entity.system.GameObject;

import java.util.ArrayList;
import java.util.List;

//классс сцены, создан для оброботки физики и т.д
public abstract class Scene {
    protected Camera camera;
    private boolean isRunning = false;

    protected List<GameObject> gameObjects = new ArrayList<>();

    public Scene() {

    }

    public void init() {

    }

    public void start() {
        for (GameObject go : gameObjects) {
            go.start();
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go) {
        if (!isRunning) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
        }
    }

    public abstract void update(float dt);
}
