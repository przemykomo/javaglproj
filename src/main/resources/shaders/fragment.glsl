#version 450

in vec2 vTexPosition;

out vec4 pixelColor;

uniform sampler2D aTexture;

void main() {
    pixelColor = texture(aTexture, vTexPosition);
}
