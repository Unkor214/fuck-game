package me.unkor.fuck.entity.components.render;

import me.unkor.fuck.entity.system.Component;
import me.unkor.render.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRender extends Component {
    private Vector4f color;
    private Vector2f[] texturePos;
    private Texture texture;

    public SpriteRender(Vector4f color) {
        this.color = color;
        this.texture = null;
    }

    public SpriteRender(Texture texture) {
        this.texture = texture;
        this.color = new Vector4f(1, 1, 1, 1);
    }

    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {

    }

    public Vector4f getColor() {
        return color;
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2f[] getTexturePos() {
        return new Vector2f[] {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1),
        };
    }
}
