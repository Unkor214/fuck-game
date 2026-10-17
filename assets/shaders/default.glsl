#type vertex
#version 330 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexPos;

uniform mat4 uProjMatrix;
uniform mat4 uViewMatrix;

out vec4 fColor;
out vec2 fTexPos;

void main() {
    fColor = aColor;
    fTexPos = aTexPos;
    gl_Position = uProjMatrix * uViewMatrix * vec4(aPos, 1.0);
}

#type fragment
#version 330 core

uniform float uTime;
uniform sampler2D TES_SAMP;

in vec4 fColor;
in vec2 fTexPos;

out vec4 color;

void main() {
    color = texture(TES_SAMP, fTexPos);
}