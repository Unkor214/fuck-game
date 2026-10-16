package me.unkor.scene;

/*
* TODO:
*  1. оброботка физичиский процессов
*/

import me.unkor.Camera;

//классс сцены, создан для оброботки физики и т.д
public abstract class Scene {
    protected Camera camera;

    public Scene() {

    }

    public void init() {

    }

    public abstract void update(float dt);
}
