package com.screendead.CubicWorldgen.graphics.lighting;

import org.joml.Vector3f;

import java.util.HashMap;

public abstract class Light {
    public float x, y, z;
    public float r, g, b;
    public float intensity;
    protected final HashMap<Integer, Float> cache = new HashMap<>();

    public Light(float x, float y, float z, float r, float g, float b, float intensity) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.r = r;
        this.g = g;
        this.b = b;
        this.intensity = intensity;
    }

    public abstract float getLightLevel(float x, float y, float z);

    public float getLightLevel(Vector3f position) {
        int hash = position.hashCode();
        if (cache.containsKey(hash)) return cache.get(hash);

        cache.put(hash, getLightLevel(position.x, position.y, position.z));
        return cache.get(hash);
    }

    public Vector3f getColor() {
        return new Vector3f(r, g, b);
    }
}
