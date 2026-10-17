package me.unkor.render;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public class Texture {
    private int textureID;
    private String pathToFile;

    public Texture(String pathToFile) {
        this.pathToFile = pathToFile;

        // генерация текстуры на gpu
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        // Установка параметров текстур
        // Повторение текстуры, если объект текстуры слишком большой
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        // Пиксилизация текстуры при увеличение (так как движок создан для таких нужд)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        // Пиксилизация текстуры при уменьшение (так как движок создан для таких нужд)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        ByteBuffer image = stbi_load(pathToFile, width, height, channels, 0);

        if (image != null) {
            if (channels.get(0) == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB,
                        width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            } else if (channels.get(0) == 4){
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA,
                        width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            } else {
                assert false : "Error : (Texture) Unknow num of channels '" + channels + "'";
            }
        } else {
            assert false : "Error : (Texture) cloud not load image '" + pathToFile + "' or is not exist";
        }

        stbi_image_free(image);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}