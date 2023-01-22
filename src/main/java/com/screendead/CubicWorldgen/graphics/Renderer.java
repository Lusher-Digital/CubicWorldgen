package com.screendead.CubicWorldgen.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class Renderer {
    private final Shader shader;
    private final Camera camera;
    private final Mesh mesh;

    public Renderer(float aspect) {
        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.1f, 0.0f);

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glFrontFace(GL_CW);
        glEnable(GL_MULTISAMPLE);

        camera = new Camera(
                new Vector3f(0.0f, 0.0f, -3.0f),
                90, 0,
                75.0f,
                aspect,
                0.1f,
                1000.0f);


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
        shader.setUniform("view", camera.getViewMatrix());
        shader.setUniform("projection", camera.getProjectionMatrix());

        // enable wireframes
//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        mesh.render();

        Shader.unbind();
    }

    public void moveCamera(float x, float y, float z) {
        camera.move(x, y, z);
    }

    public void rotateCamera(float dx, float dy) {
        camera.rotate(dx, dy);
    }

    public void destroy() {
        shader.destroy();
        mesh.destroy();
    }
}
