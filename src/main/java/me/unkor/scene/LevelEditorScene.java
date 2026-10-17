package me.unkor.scene;

import me.unkor.Camera;
import me.unkor.fuck.entity.components.FontRender;
import me.unkor.fuck.entity.components.SpriteRender;
import me.unkor.fuck.entity.system.GameObject;
import me.unkor.render.Shader;
import me.unkor.render.Texture;
import me.unkor.util.Time;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

//редактирование сцены
public class LevelEditorScene extends Scene {
    private int vaoID, vboID, eboID;
    private Shader defaultShader;
    private Texture testTexture;

    private GameObject testObj;
    private boolean firstTime = true;

    private float[] vertexArray = {
            // позиция                // цвет
            100.5f, 0.5f,  0.0f,       1.0f, 0.0f, 0.0f, 1.0f,      1, 1,  // 0
            0.5f,  100.0f,  0.0f,       0.0f, 1.0f, 0.0f, 1.0f,     0, 0,  // 1
            100.0f, 100.0f,  0.0f ,       0.0f, 0.0f, 1.0f, 1.0f,   1, 0,  // 2
            0.5f, 0.5f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f,         0, 1,  // 3
    };

    private int[] elementArray = {
            0, 1, 3,
            2, 0, 1
    };

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        System.out.println("creat test obj");
        this.testObj = new GameObject("test object");
        this.testObj.addComponent(new SpriteRender());
        this.testObj.addComponent(new FontRender());
        this.addGameObjectToScene(this.testObj);

        this.camera = new Camera(new Vector2f());
        this.testTexture = new Texture("assets/images/test.png");

        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();

        // генерация VAO и VBO
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Создание буфера с пл. точкой
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Создание VBO и загрузка вершиного буфера
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Создание цел. числ. буфера
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        int positionSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionSize + colorSize + uvSize) * Float.BYTES;

        glVertexAttribPointer(0, Float.BYTES, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes,
                positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes,
                (positionSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {
        defaultShader.use();

        defaultShader.uploadTexture("TEX_SAMP", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProjMatrix", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uViewMatrix", camera.getViewMatrix());
        defaultShader.uploadUniform("f", "uTime", Time.getTime());

        glBindVertexArray(vaoID);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();

        if (firstTime) {
            System.out.println("Create game object");
            GameObject go = new GameObject("Game test");
            go.addComponent(new SpriteRender());
            this.addGameObjectToScene(go);
            firstTime = false;
        }

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
    }
}