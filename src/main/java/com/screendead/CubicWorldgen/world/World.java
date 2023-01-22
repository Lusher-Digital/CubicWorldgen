package com.screendead.CubicWorldgen.world;

import com.screendead.CubicWorldgen.graphics.Mesh;

import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class World {
    private final Mesh mesh;

    public World() {
        BlockType block = BlockType.GRASS;

        float[] vertices = new float[]{
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, 0.5f
        };

        float[] normals = new float[]{
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f
        };

        float[] colors = new float[]{
                block.getR(), block.getG(), block.getB(),
                block.getR(), block.getG(), block.getB(),
                block.getR(), block.getG(), block.getB(),
                block.getR(), block.getG(), block.getB(),
                block.getR(), block.getG(), block.getB(),
                block.getR(), block.getG(), block.getB(),
                block.getR(), block.getG(), block.getB(),
                block.getR(), block.getG(), block.getB()
        };

        int[] indices = new int[]{
                0, 1, 2,
                2, 1, 3,
                2, 3, 6,
                6, 3, 7,
                6, 7, 4,
                4, 7, 5,
                4, 5, 0,
                0, 5, 1,
                4, 0, 6,
                6, 0, 2,
                1, 5, 3,
                3, 5, 7
        };

        mesh = new Mesh(GL_STATIC_DRAW, vertices, normals, colors, indices);
    }

    public void render() {
        mesh.render();
    }

    public void destroy() {
        mesh.destroy();
    }
}
