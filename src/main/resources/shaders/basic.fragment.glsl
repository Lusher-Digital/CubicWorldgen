#version 410

in vec3 FragPos;
in vec3 Normal;
in vec3 Color;
in float LightLevel;

out vec4 color;

void main()
{
    vec3 result = (Normal + FragPos / 16) * 0.5;
    color = vec4(result, 1.0);
}
