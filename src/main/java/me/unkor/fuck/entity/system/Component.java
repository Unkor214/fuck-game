package me.unkor.fuck.entity.system;

public abstract class Component {
    public GameObject gameObject = null;

    public void start() {

    }

    public abstract void update(float dt);
}
