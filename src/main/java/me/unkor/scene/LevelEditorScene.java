package me.unkor.scene;

import me.unkor.Camera;
import me.unkor.Transform;
import me.unkor.fuck.entity.components.render.SpriteRender;
import me.unkor.fuck.entity.system.Component;
import me.unkor.fuck.entity.system.GameObject;
import me.unkor.util.AssetsPool;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.assimp.AIVertexWeight;

//редактирование сцены
public class LevelEditorScene extends Scene {

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(-250, 0));

        GameObject obj = new GameObject("object 1", new Transform(new Vector2f(100, 100),
                new Vector2f(256, 256)));
        obj.addComponent(new SpriteRender(AssetsPool.getTextures("assets/images/test.png")));
        this.addGameObjectToScene(obj);

        loadContent();
    }

    @Override
    public void loadContent() {
        AssetsPool.getShader("assets/shaders/default.glsl");
    }

    @Override
    public void update(float dt) {
        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }
}