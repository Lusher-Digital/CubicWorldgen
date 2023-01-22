#version 410

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 color;

out vec3 FragPos;
out vec3 Color;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    FragPos = vec3(model * vec4(position, 1.0f));
    Color = color;

    gl_Position = projection * view * model * vec4(position, 1.0f);
}
