#version 450

in vec2 vTexPosition;

out vec4 outColor;

uniform sampler2D aTexture;

void main() {
    outColor = texture(aTexture, vTexPosition);
}
