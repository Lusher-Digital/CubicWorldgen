#version 410

layout (location = 0) in vec3 positions;
layout (location = 1) in vec3 colors;
layout (location = 2) in float lightLevels;
layout (location = 3) in vec3 lightColors;

out vec3 FragPos;
out vec3 Color;
out float LightLevel;
out vec3 LightColor;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

float simplex_noise(float x, float y, float z)
{
    float s = (x + y + z) / 3.0;
    float i = floor(x + s);
    float j = floor(y + s);
    float k = floor(z + s);

    float t = (i + j + k) / 6.0;
    float X0 = i - t;
    float Y0 = j - t;
    float Z0 = k - t;

    float x0 = x - X0;
    float y0 = y - Y0;
    float z0 = z - Z0;

    int i1, j1, k1;
    int i2, j2, k2;

    if (x0 >= y0) {
        if (y0 >= z0) {
            i1 = 1; j1 = 0; k1 = 0;
            i2 = 1; j2 = 1; k2 = 0;
        } else if (x0 >= z0) {
            i1 = 1; j1 = 0; k1 = 0;
            i2 = 1; j2 = 0; k2 = 1;
        } else {
            i1 = 0; j1 = 0; k1 = 1;
            i2 = 1; j2 = 0; k2 = 1;
        }
    } else {
        if (y0 < z0) {
            i1 = 0; j1 = 0; k1 = 1;
            i2 = 0; j2 = 1; k2 = 1;
        } else if (x0 < z0) {
            i1 = 0; j1 = 1; k1 = 0;
            i2 = 0; j2 = 1; k2 = 1;
        } else {
            i1 = 0; j1 = 1; k1 = 0;
            i2 = 1; j2 = 1; k2 = 0;
        }
    }

    float x1 = x0 - i1 + 1.0 / 6.0;
    float y1 = y0 - j1 + 1.0 / 6.0;
    float z1 = z0 - k1 + 1.0 / 6.0;
    float x2 = x0 - i2 + 2.0 / 6.0;
    float y2 = y0 - j2 + 2.0 / 6.0;
    float z2 = z0 - k2 + 2.0 / 6.0;
    float x3 = x0 - 1.0 + 3.0 / 6.0;
    float y3 = y0 - 1.0 + 3.0 / 6.0;
    float z3 = z0 - 1.0 + 3.0 / 6.0;

    float n0, n1, n2, n3;

    float t0 = 0.6 - x0 * x0 - y0 * y0 - z0 * z0;
    if (t0 < 0.0) {
        n0 = 0.0;
    } else {
        t0 *= t0;
        n0 = t0 * t0 * dot(vec3(x0, y0, z0), vec3(i, j, k));
    }

    float t1 = 0.6 - x1 * x1 - y1 * y1 - z1 * z1;
    if (t1 < 0.0) {
        n1 = 0.0;
    } else {
        t1 *= t1;
        n1 = t1 * t1 * dot(vec3(x1, y1, z1), vec3(i + i1, j + j1, k + k1));
    }

float t2 = 0.6 - x2 * x2 - y2 * y2 - z2 * z2;
    if (t2 < 0.0) {
        n2 = 0.0;
    } else {
        t2 *= t2;
        n2 = t2 * t2 * dot(vec3(x2, y2, z2), vec3(i + i2, j + j2, k + k2));
    }

    float t3 = 0.6 - x3 * x3 - y3 * y3 - z3 * z3;
    if (t3 < 0.0) {
        n3 = 0.0;
    } else {
        t3 *= t3;
        n3 = t3 * t3 * dot(vec3(x3, y3, z3), vec3(i + 1, j + 1, k + 1));
    }

    return 32.0 * (n0 + n1 + n2 + n3);
}

void main()
{
    FragPos = vec3(model * vec4(positions, 1.0));
    Color = colors;
    LightLevel = lightLevels;
    LightColor = lightColors;

    vec3 newPositions = positions;

    newPositions += vec3(
        simplex_noise(positions.x * 0.1, positions.y * 0.1, positions.z * 0.1),
        simplex_noise(positions.x * 0.1, positions.y * 0.1, positions.z * 0.1),
        simplex_noise(positions.x * 0.1, positions.y * 0.1, positions.z * 0.1)
    ) * 0.25;

    gl_Position = projection * view * model * vec4(newPositions, 1.0);
}
