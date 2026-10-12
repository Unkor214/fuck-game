package me.unkor.scene;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

//редактирование сцены
public class LevelEditorScene extends Scene {
    private String vertexShaderSrc = "#version 330 core\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main() {\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}\n";

    private String fragmentSgaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main() {\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexID, fragmentID, shaderProgram;
    private int vaoID, vboID, eboID;

    private float[] vertexArray = {
            // позиция                // цвет
            0.1f, -0.1f,  0.0f,       1.0f, 0.0f, 0.0f, 1.0f,// 0
            -0.1f,  0.1f,  0.0f,       0.0f, 1.0f, 0.0f, 1.0f,// 1
            0.1f, 0.1f,  0.0f ,       0.0f, 0.0f, 1.0f, 1.0f,// 2
            -0.1f, -0.1f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f,// 3
    };

    private int[] elementArray = {
            0, 1, 3,
            2, 0, 1
    };

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        // компоновка и линковка

        // Шейдер вершин
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Ссылка шейдера в Сибирь
        glShaderSource(vertexID, vertexShaderSrc);
        glCompileShader(vertexID);

        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR : 'default.glsl'\n\tVertex shader compiling err");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // Шейдер фрагментов
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Ссылка шейдера в Сибирь
        glShaderSource(fragmentID, fragmentSgaderSrc);
        glCompileShader(fragmentID);

        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR : 'default.glsl'\n\tFragment shader compiling err");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // Линковка
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR : 'default.glsl'\n\tShader linking err");
            System.out.println(glGetShaderInfoLog(shaderProgram, len));
            assert false : "";
        }

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

        // Создание чел. числ. буфера
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
        glUseProgram(shaderProgram);
        glBindVertexArray(vaoID);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        glUseProgram(0);
    }
}