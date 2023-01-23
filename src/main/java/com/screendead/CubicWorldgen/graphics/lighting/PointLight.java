package com.screendead.CubicWorldgen.graphics.lighting;

import org.joml.Vector3f;

public class PointLight extends Light {
    public PointLight(float x, float y, float z, float r, float g, float b, float intensity) {
        super(x, y, z, r, g, b, intensity);
    }

    public PointLight(Vector3f position, float r, float g, float b, float intensity) {
        super(position.x, position.y, position.z, r, g, b, intensity);
    }

    @Override
    public float getLightLevel(float x, float y, float z) {
        int hash = (int) (x * 1000) + (int) (y * 1000) + (int) (z * 1000);
        if (cache.containsKey(hash)) return cache.get(hash);

        float dx = this.x - x;
        float dy = this.y - y;
        float dz = this.z - z;
        float distance = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);

        cache.put(hash, intensity / (distance * distance));
        return cache.get(hash);
    }
}
