package me.unkor.scene;

/*
* TODO:
*  1. оброботка физичиский процессов
*/

import me.unkor.Camera;
import me.unkor.Window;
import me.unkor.fuck.entity.system.GameObject;
import me.unkor.render.Render;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Render renderer = new Render();
    protected Camera camera;
    //создано для первого запуска, чтобы добавить объект на сцену
    private boolean isRunning = false;

    //список всех игровых объектов на сцене
    protected List<GameObject> gameObjects = new ArrayList<>();

    public Scene() {

    }

    public void init() {

    }

    //нужен для запуска всех объектов на сцене
    public void start() {
        for (GameObject go : gameObjects) {
            go.start();
            this.renderer.add(go);
        }
        isRunning = true;
    }

    //нужен для добавления объекта на сцену
    public void addGameObjectToScene(GameObject go) {
        if (!isRunning) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
        }
    }

    public abstract void loadContent();

    public abstract void update(float dt);

    public Camera getCamera() {
        return this.camera;
    }
}
