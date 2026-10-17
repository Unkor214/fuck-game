package me.unkor.render;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.io.Serializable;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int shaderProgramID;
    private boolean usage = false;

    private String vertexSrc;
    private String fragmentSrc;
    private String pathToFile;

    public Shader (String pathToFile) {
        this.pathToFile = pathToFile;
        try {
            String src = new String(Files.readAllBytes(Paths.get(pathToFile)));
            String[] splitStr = src.split("(#type)( )+([a-zA-Z]+)");

            // Первый патерн (#type 'pattern')
            int index = src.indexOf("#type") + 6;
            int eol = src.indexOf("\r\n", index);
            String firstPattern = src.substring(index, eol).trim();

            // Второй патерн (#type 'pattern1')
            index = src.indexOf("#type", eol) + 6;
            eol = src.indexOf("\r\n", index);
            String secondPattern = src.substring(index, eol).trim();

            if (firstPattern.equals("vertex")) {
                vertexSrc = splitStr[1];
            } else if (firstPattern.equals("fragment")) {
                fragmentSrc = splitStr[1];
            } else {
                throw new IOException("Unexpected token '" + firstPattern + "' in '" + pathToFile + "'");
            }

            if (secondPattern.equals("vertex")) {
                vertexSrc = splitStr[2];
            } else if (secondPattern.equals("fragment")) {
                fragmentSrc = splitStr[2];
            } else {
                throw new IOException("Unexpected token '" + secondPattern + "' in '" + pathToFile + "'");
            }
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error : Shader path '" + pathToFile + "' is invalid";
        }
    }

    public void compile() {
        // компоновка и линковка
        int vertexID, fragmentID;

        // Шейдер вершин
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Ссылка шейдера в Сибирь
        glShaderSource(vertexID, vertexSrc);
        glCompileShader(vertexID);

        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR : '" + pathToFile + "'\n\tVertex shader compiling err");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // Шейдер фрагментов
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Ссылка шейдера в Сибирь
        glShaderSource(fragmentID, fragmentSrc);
        glCompileShader(fragmentID);

        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR : '" + pathToFile + "'\n\tFragment shader compiling err");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // Линковка
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR : '" + pathToFile + "'\n\tShader linking err");
            System.out.println(glGetShaderInfoLog(shaderProgramID, len));
            assert false : "";
        }
    }

    public void use() {
        if (!usage) {
            glUseProgram(shaderProgramID);
            usage = true;
        }
    }

    public void detach() {
        glUseProgram(0);
        usage = false;
    }

    public <T> void uploadUniform(String type, String name, T value) {
        try {
            switch (type) {
                case "i" :
                    uploadInt(name, (Integer) value);
                    break;
                case "f" :
                    uploadFloat(name, (float) value);
                    break;
                case "mat4f" :
                    uploadMat4f(name, (Matrix4f) value);
                    break;
                case "mat3f" :
                    uploadMat3f(name, (Matrix3f) value);
                    break;
                case "vec4f" :
                    uploadVec4f(name, (Vector4f) value);
                    break;
                case "vec3f" :
                    uploadVec3f(name, (Vector3f) value);
                    break;
                case "vec2f" :
                    uploadVec2f(name, (Vector2f) value);
                    break;
                default :
                    assert false : "Error : invalid for upload uniform";
                    break;
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
            assert false : "Error : data type and user chose data, in 'uploadUniform', has mismatch";
        }
    }

    public void uploadMat4f(String name, Matrix4f mat4f) {
        int varLocation = glGetUniformLocation(shaderProgramID, name);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4f.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMat3f(String name, Matrix3f mat3f) {
        int varLocation = glGetUniformLocation(shaderProgramID, name);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3f.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    public void uploadVec4f(String name, Vector4f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadVec3f(String name, Vector3f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    public void uploadVec2f(String name, Vector2f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform2f(varLocation, vec.x, vec.y);
    }

    public void uploadFloat(String name, float value) {
        int varLocation = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform1f(varLocation, value);
    }

    public void uploadInt(String name, int value) {
        int varLocation = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform1i(varLocation, value);
    }
}
