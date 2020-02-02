#version 450

in vec3 position;
in vec2 texPosition;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

out vec2 vTexPosition;

void main() {
    gl_Position = projection * view * model * vec4(position, 1.0);
    vTexPosition = texPosition;
}
