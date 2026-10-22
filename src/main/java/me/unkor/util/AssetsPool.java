package me.unkor.util;

import me.unkor.render.Shader;
import me.unkor.render.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetsPool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();

    public static Shader getShader (String resourceName) {
        File file = new File(resourceName);

        if (shaders.containsKey(file.getAbsolutePath())) {
            return shaders.get(file.getAbsolutePath());
        } else {
            Shader shader = new Shader(resourceName);
            shader.compile();
            AssetsPool.shaders.put(file.getAbsolutePath(), shader);

            return shader;
        }
    }

    public static Texture getTextures(String resourceName) {
        File file = new File(resourceName);

        if (AssetsPool.textures.containsKey(file.getAbsolutePath())) {
            return AssetsPool.textures.get(file.getAbsolutePath());
        } else {
            Texture texture = new Texture(resourceName);
            AssetsPool.textures.put(file.getAbsolutePath(), texture);

            return AssetsPool.textures.get(resourceName);
        }
    }
}
