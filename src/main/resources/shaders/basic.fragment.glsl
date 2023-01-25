#version 410

in vec3 FragPos;
in vec3 Normal;
in vec3 Color;

out vec4 color;

void main()
{
    color = vec4(Color, 1.0);
}
