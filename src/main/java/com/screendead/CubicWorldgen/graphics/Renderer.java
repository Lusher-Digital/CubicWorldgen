package com.screendead.CubicWorldgen.graphics;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class Renderer {
    private final Shader shader;
    private final Mesh mesh;
    private final int frameBufferWidth;
    private final int frameBufferHeight;

    public Renderer(int frameBufferWidth, int frameBufferHeight) {
        this.frameBufferWidth = frameBufferWidth;
        this.frameBufferHeight = frameBufferHeight;

        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.1f, 0.0f);

        float[] vertices = new float[]{
                -0.5f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f, 0.5f, 0.0f
        };

        float[] normals = new float[]{
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f
        };

        float[] colors = new float[]{
                1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
        };

        int[] indices = new int[]{
                0, 1, 2,
                2, 3, 0
        };

        mesh = new Mesh(GL_STATIC_DRAW, vertices, normals, colors, indices);

        shader = new Shader(
                "/shaders/basic.vertex.glsl",
                "/shaders/basic.fragment.glsl");
        shader.addUniform("model");
        shader.addUniform("view");
        shader.addUniform("projection");
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        shader.bind();

        shader.setUniform("model", new Matrix4f().identity());
        shader.setUniform("view", new Matrix4f().translate(0.0f, 0.0f, -3.0f).lookAt(
                0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 1.0f, 0.0f));
        shader.setUniform("projection", new Matrix4f().perspective(
                (float) Math.toRadians(45.0f),
                (float) frameBufferWidth / (float) frameBufferHeight,
                0.1f,
                100.0f));

        // enable wireframes
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        mesh.render();

        Shader.unbind();
    }

    public void destroy() {
        shader.destroy();
        mesh.destroy();
    }
}
