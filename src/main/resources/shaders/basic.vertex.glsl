#version 410

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec3 color;
layout (location = 3) in float lightLevel;

out vec3 FragPos;
out vec3 Normal;
out vec3 Color;
out float LightLevel;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    FragPos = vec3(model * vec4(position, 1.0));
    Normal = mat3(transpose(inverse(model))) * normal;
    Color = color;
    LightLevel = lightLevel;

    gl_Position = projection * view * model * vec4(position, 1.0f);
}
