#version 410

in vec3 FragPos;
in vec3 Color;
in float LightLevel;
in vec3 LightColor;

out vec4 color;

float random(int seed)
{
    return fract(sin(dot(vec2(seed, seed), vec2(12.9898, 78.233))) * 43758.5453);
}

void main()
{
    float blockX = FragPos.x - floor(FragPos.x);
    float blockY = FragPos.y - floor(FragPos.y);
    float blockZ = FragPos.z - floor(FragPos.z);

    vec3 result;
    if (blockX < 0.5 && blockY < 0.5 && blockZ < 0.5) {
        result = vec3(blockX, blockY, blockZ) * 2;
    } else if (blockX < 0.5 && blockY < 0.5) {
        result = vec3(blockX, blockY, 1.0 - blockZ) * 2;
    } else if (blockX < 0.5 && blockZ < 0.5) {
        result = vec3(blockX, 1.0 - blockY, blockZ) * 2;
    } else if (blockY < 0.5 && blockZ < 0.5) {
        result = vec3(1.0 - blockX, blockY, blockZ) * 2;
    } else if (blockX < 0.5) {
        result = vec3(blockX, 1.0 - blockY, 1.0 - blockZ) * 2;
    } else if (blockY < 0.5) {
        result = vec3(1.0 - blockX, blockY, 1.0 - blockZ) * 2;
    } else if (blockZ < 0.5) {
        result = vec3(1.0 - blockX, 1.0 - blockY, blockZ) * 2;
    } else {
        result = Color;
    }

    color = vec4(result, 1.0);
}
