package com.screendead.CubicWorldgen.graphics;

import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.ArrayList;

public class Face {

    private final Vector3i blockPosition;
    private final Facing facing;

    public Face(Vector3i blockPosition, Facing facing) {
        this.blockPosition = blockPosition;
        this.facing = facing;
    }

    public ArrayList<Vector3f> getFace() {
        ArrayList<Vector3f> vertices = new ArrayList<>();

        switch (facing) {
            case NORTH -> {
                vertices.add(new Vector3f(blockPosition.x, blockPosition.y, blockPosition.z));
                vertices.add(new Vector3f(blockPosition.x + 1, blockPosition.y, blockPosition.z));
                vertices.add(new Vector3f(blockPosition.x + 1, blockPosition.y + 1, blockPosition.z));
                vertices.add(new Vector3f(blockPosition.x, blockPosition.y + 1, blockPosition.z));
            }
            case SOUTH -> {
                vertices.add(new Vector3f(blockPosition.x, blockPosition.y, blockPosition.z + 1));
                vertices.add(new Vector3f(blockPosition.x + 1, blockPosition.y, blockPosition.z + 1));
                vertices.add(new Vector3f(blockPosition.x + 1, blockPosition.y + 1, blockPosition.z + 1));
                vertices.add(new Vector3f(blockPosition.x, blockPosition.y + 1, blockPosition.z + 1));
            }
            case EAST -> {
                vertices.add(new Vector3f(blockPosition.x + 1, blockPosition.y, blockPosition.z));
                vertices.add(new Vector3f(blockPosition.x + 1, blockPosition.y, blockPosition.z + 1));
                vertices.add(new Vector3f(blockPosition.x + 1, blockPosition.y + 1, blockPosition.z + 1));
                vertices.add(new Vector3f(blockPosition.x + 1, blockPosition.y + 1, blockPosition.z));
            }
            case WEST -> {
                vertices.add(new Vector3f(blockPosition.x, blockPosition.y, blockPosition.z));
                vertices.add(new Vector3f(blockPosition.x, blockPosition.y, blockPosition.z + 1));
                vertices.add(new Vector3f(blockPosition.x, blockPosition.y + 1, blockPosition.z + 1));
                vertices.add(new Vector3f(blockPosition.x, blockPosition.y + 1, blockPosition.z));
            }
            case UP -> {
                vertices.add(new Vector3f(blockPosition.x, blockPosition.y + 1, blockPosition.z));
                vertices.add(new Vector3f(blockPosition.x + 1, blockPosition.y + 1, blockPosition.z));
                vertices.add(new Vector3f(blockPosition.x + 1, blockPosition.y + 1, blockPosition.z + 1));
                vertices.add(new Vector3f(blockPosition.x, blockPosition.y + 1, blockPosition.z + 1));
            }
            case DOWN -> {
                vertices.add(new Vector3f(blockPosition.x, blockPosition.y, blockPosition.z));
                vertices.add(new Vector3f(blockPosition.x + 1, blockPosition.y, blockPosition.z));
                vertices.add(new Vector3f(blockPosition.x + 1, blockPosition.y, blockPosition.z + 1));
                vertices.add(new Vector3f(blockPosition.x, blockPosition.y, blockPosition.z + 1));
            }
        }

        return vertices;
    }
}
