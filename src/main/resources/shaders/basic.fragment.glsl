#version 410

in vec3 FragPos;
in vec3 Normal;
in vec3 Color;

out vec4 color;

void main()
{
    float depth = gl_FragCoord.z; // depth value from the depth buffer
    vec3 result = Color * (1 - depth * depth * depth);
    // store the result
    color = vec4(result, 1.0);
}
