package me.unkor.scene;

import me.unkor.Camera;
import me.unkor.render.Shader;
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

    private float[] vertexArray = {
            // позиция                // цвет
            100.5f, 0.5f,  0.0f,       1.0f, 0.0f, 0.0f, 1.0f,// 0
            0.5f,  100.0f,  0.0f,       0.0f, 1.0f, 0.0f, 1.0f,// 1
            100.0f, 100.0f,  0.0f ,       0.0f, 0.0f, 1.0f, 1.0f,// 2
            0.5f, 0.5f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f,// 3
    };

    private int[] elementArray = {
            0, 1, 3,
            2, 0, 1
    };

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());

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
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;

        glVertexAttribPointer(0, floatSizeBytes, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes,
                positionSize * floatSizeBytes);
        glEnableVertexAttribArray(1);

    }

    @Override
    public void update(float dt) {
        camera.position.x -= dt * 50.0f;

        defaultShader.use();
        defaultShader.uploadMat4f("uProjMatrix", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uViewMatrix", camera.getViewMatrix());

        glBindVertexArray(vaoID);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();
    }
}