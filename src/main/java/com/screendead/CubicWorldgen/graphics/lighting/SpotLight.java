package com.screendead.CubicWorldgen.graphics.lighting;

import org.joml.Vector3f;

public class SpotLight extends Light {
    public float effectiveAngle;
    public float horizontalRotation;
    public float verticalRotation;

    public SpotLight(float x, float y, float z, float r, float g, float b, float intensity, float effectiveAngle, float horizontalAngle, float verticalAngle) {
        super(x, y, z, r, g, b, intensity);
        this.effectiveAngle = effectiveAngle;
        this.horizontalRotation = horizontalAngle;
        this.verticalRotation = verticalAngle;
    }

    @Override
    public float getLightLevel(float x, float y, float z) {
        int hash = (int) (x * 1000) + (int) (y * 1000) + (int) (z * 1000);
        if (cache.containsKey(hash)) return cache.get(hash);

        float dx = this.x - x;
        float dy = this.y - y;
        float dz = this.z - z;

        float distance = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);

        Vector3f look = new Vector3f(0, 0, 1);
        Vector3f up = new Vector3f(0, 1, 0);
        Vector3f right = new Vector3f();

        up.cross(look, right);
        right.normalize();

        look.rotateAxis((float) Math.toRadians(verticalRotation), right.x, right.y, right.z);
        look.rotateAxis((float) Math.toRadians(horizontalRotation), up.x, up.y, up.z);

        float angle = (float) Math.toDegrees(Math.acos(look.dot(new Vector3f(dx, dy, dz)) / (look.length() * distance)));
        if (angle > effectiveAngle) {
            cache.put(hash, 0f);
            return 0f;
        }

        cache.put(hash, intensity / (distance * distance));
        return cache.get(hash);
    }
}
