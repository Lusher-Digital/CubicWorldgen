package com.screendead.CubicWorldgen.graphics.noise;

@SuppressWarnings("unused")
public abstract class Noise {
    public abstract float noise(float x, float y, float z);

    public float noise(float x, float y) {
        return noise(x, y, 0);
    }

    public float noise(float x) {
        return noise(x, 0, 0);
    }

    public float octave(float x, float y, float z, int octaves, float persistence) {
        float total = 0;
        float frequency = 1;
        float amplitude = 1;
        float maxValue = 0;

        for (int i = 0; i < octaves; i++) {
            total += noise(x * frequency, y * frequency, z * frequency) * amplitude;

            maxValue += amplitude;

            amplitude *= persistence;
            frequency *= 2;
        }

        return total / maxValue;
    }
}
