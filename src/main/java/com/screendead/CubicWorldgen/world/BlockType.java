package com.screendead.CubicWorldgen.world;

import org.joml.Vector3f;

@SuppressWarnings("unused")
public enum BlockType {
    UNKNOWN(0, 0, 0),
    AIR(0, 0, 0),
    GRASS(0, 255, 0),
    DIRT(128, 64, 0),
    STONE(128, 128, 128),
    WATER(0, 0, 255),
    SAND(255, 255, 0),
    WOOD(128, 64, 0),
    LEAVES(0, 128, 0),
    BEDROCK(0, 0, 0);

    private final int r, g, b;

    BlockType(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public float getR() {
        return r / 255f;
    }

    public float getG() {
        return g / 255f;
    }

    public float getB() {
        return b / 255f;
    }

    public Vector3f getColor() {
        return new Vector3f(getR(), getG(), getB());
    }
}
