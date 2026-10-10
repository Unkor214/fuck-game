package me.unkor.scene;

import me.unkor.Window;
import me.unkor.listeners.KeyListener;

import java.awt.event.KeyEvent;

import static org.lwjgl.opengl.GL20.*;

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
        glShaderSource(fragmentID, vertexShaderSrc);
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
    }

    @Override
    public void update(float dt) {

    }
}
