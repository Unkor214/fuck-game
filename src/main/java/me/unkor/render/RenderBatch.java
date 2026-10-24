package me.unkor.render;

import me.unkor.Window;
import me.unkor.fuck.entity.components.render.SpriteRender;
import me.unkor.fuck.entity.system.Component;
import me.unkor.util.AssetsPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

import javax.swing.plaf.basic.BasicSeparatorUI;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public class RenderBatch {
    /*
    * Verticals
    * ------------
    *
    * Position          color                               texture pos   texID
    * float x, float y, float r, float g, float b, float a, float, float, float
    * */
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEXTURE_POS_SIZE = 2;
    private final int TEXTURE_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEXTURE_POS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEXTURE_ID_OFFSET = TEXTURE_POS_OFFSET + TEXTURE_POS_SIZE * Float.BYTES;

    private final int VERTEX_SIZE = 9;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRender[] sprites;
    private int numOfSprites;
    private float[] vertices;
    private int[] texSlot = {0, 1, 2, 3, 4, 5, 6, 7};

    private boolean hasRoom;

    private List<Texture> textures;
    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;

    public RenderBatch(int maxBatchSize) {
        shader = AssetsPool.getShader("assets/shaders/default.glsl");
        this.sprites = new SpriteRender[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        this.numOfSprites = 0;
        this.hasRoom = true;
        this.textures = new ArrayList<>();
    }

    public void start() {
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false,
                VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false,
                VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEXTURE_POS_SIZE, GL_FLOAT, false,
                VERTEX_SIZE_BYTES, TEXTURE_POS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEXTURE_ID_SIZE, GL_FLOAT, false,
                VERTEX_SIZE_BYTES, TEXTURE_ID_OFFSET);
        glEnableVertexAttribArray(3);
    }

    public void addSprite(SpriteRender spriteRender) {
        int index = this.numOfSprites;
        this.sprites[index] = spriteRender;
        this.numOfSprites ++;

        if (spriteRender.getTexture() != null) {
            if (!textures.contains(spriteRender.getTexture())) {
                textures.add(spriteRender.getTexture());
            }
        }

        loadVertexProperties(index);

        if (numOfSprites >= this.maxBatchSize) {
            hasRoom = false;
        }
    }

    public void render() {
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        shader.use();
        shader.uploadMat4f("uProjMatrix", Window.getCurentScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uViewMatrix", Window.getCurentScene().getCamera().getViewMatrix());

        for (int i=0; i < textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }
        shader.uploadIntArray("uTextures", texSlot);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numOfSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        for (int i=0; i < textures.size(); i++) {
            textures.get(i).unbind();
        }

        shader.detach();
    }

    private void loadVertexProperties(int index) {
        SpriteRender spriteRender = this.sprites[index];

        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = spriteRender.getColor();
        Vector2f[] texturePos = spriteRender.getTexturePos();

        int texID = 0;
        if (spriteRender.getTexture() != null) {
            for (int i=0; i < textures.size(); i++) {
                if (textures.get(i) == spriteRender.getTexture()) {
                    texID = i + 1;
                    break;
                }
            }
        }

        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for (int i=0; i < 4; i++) {
            if (i == 1) {
                yAdd = 0.0f;
            } else if (i == 2) {
                xAdd = 0.0f;
            } else if (i ==3) {
                yAdd = 1.0f;
            }

            //load pos
            vertices[offset] = spriteRender.gameObject.transform.position.x + (
                    xAdd * spriteRender.gameObject.transform.scale.x);
            vertices[offset + 1] = spriteRender.gameObject.transform.position.y + (
                    yAdd * spriteRender.gameObject.transform.scale.y);

            //load color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            //load texture pos
            vertices[offset + 6] = texturePos[i].x;
            vertices[offset + 7] = texturePos[i].y;

            //load texID
            vertices[offset + 8] = texID;

            offset += VERTEX_SIZE;
        }
    }

    private int[] generateIndices() {
        int[] elements = new int[6 * maxBatchSize];
        for (int i=0; i < maxBatchSize; i++) {
            loadElementIndices(elements, i);
        }

        return elements;
    }

    private void loadElementIndices(int[] el, int index) {
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        // 3, 2, 0, 0, 2, 1       7, 6, 4, 4, 6, 5
        // Триугольник 1
        el[offsetArrayIndex] = offset + 3;
        el[offsetArrayIndex + 1] = offset + 2;
        el[offsetArrayIndex + 2] = offset;

        //Триугольник 2
        el[offsetArrayIndex + 3] = offset;
        el[offsetArrayIndex + 4] = offset + 2;
        el[offsetArrayIndex + 5] = offset + 1;
    }

    public boolean hasRoom() {
        return this.hasRoom;
    }
}