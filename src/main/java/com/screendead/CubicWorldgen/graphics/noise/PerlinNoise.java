package com.screendead.CubicWorldgen.graphics.noise;

public class PerlinNoise extends Noise {
    private final int[] permutationTable;
    private final float scale;

    public PerlinNoise(float scale) {
        this.scale = scale;

        permutationTable = new int[512];
        for (int i = 0; i < 256; i++) {
            permutationTable[i] = i;
        }

        for (int i = 0; i < 256; i++) {
            int j = (int) (Math.random() * 256);
            int temp = permutationTable[i];
            permutationTable[i] = permutationTable[j];
            permutationTable[j] = temp;
        }

        for (int i = 0; i < 256; i++) {
            permutationTable[i + 256] = permutationTable[i];
        }
    }

    private float fade(float t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private float lerp(float t, float a, float b) {
        return a + t * (b - a);
    }

    private float grad(int hash, float x, float y, float z) {
        int h = hash & 15;
        float u = h < 8 ? x : y;
        float v = h < 4 ? y : h == 12 || h == 14 ? x : z;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    @Override
    public float noise(float x, float y, float z) {
        x *= scale;
        y *= scale;
        z *= scale;

        int X = (int) Math.floor(x) & 255;
        int Y = (int) Math.floor(y) & 255;
        int Z = (int) Math.floor(z) & 255;

        x -= Math.floor(x);
        y -= Math.floor(y);
        z -= Math.floor(z);

        float u = fade(x);
        float v = fade(y);
        float w = fade(z);

        int A = permutationTable[X] + Y;
        int AA = permutationTable[A] + Z;
        int AB = permutationTable[A + 1] + Z;
        int B = permutationTable[X + 1] + Y;
        int BA = permutationTable[B] + Z;
        int BB = permutationTable[B + 1] + Z;

        return lerp(w, lerp(v, lerp(u,
                grad(permutationTable[AA], x, y, z),
                grad(permutationTable[BA], x - 1, y, z)),
                lerp(u,
                        grad(permutationTable[AB], x, y - 1, z),
                        grad(permutationTable[BB], x - 1, y - 1, z))),
                lerp(v,
                        lerp(u,
                                grad(permutationTable[AA + 1], x, y, z - 1),
                                grad(permutationTable[BA + 1], x - 1, y, z - 1)),
                        lerp(u, grad(permutationTable[AB + 1], x, y - 1, z - 1),
                                grad(permutationTable[BB + 1], x - 1, y - 1, z - 1))));
    }
}
