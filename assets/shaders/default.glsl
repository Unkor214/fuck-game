#type vertex
#version 330 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexturesPos;
layout (location=3) in float aTexId;

uniform mat4 uProjMatrix;
uniform mat4 uViewMatrix;

out vec4 fColor;
out vec2 fTexturePos;
out float fTexId;

void main() {
    fColor = aColor;
    fTexturePos = aTexturesPos;
    fTexId = aTexId;

    gl_Position = uProjMatrix * uViewMatrix * vec4(aPos, 1.0);
}

#type fragment
#version 330 core

in vec4 fColor;
in vec2 fTexturePos;
in float fTexId;

uniform sampler2D uTextures[8];

out vec4 color;

void main() {
    if (fTexId > 0) {
        int id = int(fTexId);
        color = fColor * texture(uTextures[id], fTexturePos);
    } else {
        color = fColor;
    }
}#type vertex
#version 330 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexturesPos;
layout (location=3) in float aTexId;

uniform mat4 uProjMatrix;
uniform mat4 uViewMatrix;

out vec4 fColor;
out vec2 fTexturePos;
out float fTexId;

void main() {
    fColor = aColor;
    fTexturePos = aTexturesPos;
    fTexId = aTexId;

    gl_Position = uProjMatrix * uViewMatrix * vec4(aPos, 1.0);
}

#type fragment
#version 330 core

in vec4 fColor;
in vec2 fTexturePos;
in float fTexId;

uniform sampler2D uTextures[8];

out vec4 color;

void main() {
    if (fTexId > 0) {
        int id = int(fTexId);
        color = fColor * texture(uTextures[id], fTexturePos);
    } else {
        color = fColor;
    }
}