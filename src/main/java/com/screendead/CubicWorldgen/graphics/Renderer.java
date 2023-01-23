package com.screendead.CubicWorldgen.graphics;

import com.screendead.CubicWorldgen.graphics.lighting.Light;
import com.screendead.CubicWorldgen.graphics.lighting.PointLight;
import com.screendead.CubicWorldgen.world.World;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    private final Shader shader;
    private final Camera camera;
    private final World world;

    public boolean wireframe = false;

    public Renderer(float aspect) {
        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        glEnable(GL_DEPTH_TEST);
//        glEnable(GL_CULL_FACE);
//        glCullFace(GL_BACK);
//        glFrontFace(GL_CCW);

        world = new World();

        camera = new Camera(
                new Vector3f(-4.0f, 20.0f, -4.0f),
                45, -40,
                75.0f,
                aspect,
                0.1f,
                1000.0f);

        shader = new Shader(
                "/shaders/basic.vertex.glsl",
                "/shaders/basic.fragment.glsl");
        shader.addUniform("model");
        shader.addUniform("view");
        shader.addUniform("projection");
    }

    public void update(int ticksToComplete) {
        camera.update(ticksToComplete);
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        shader.bind();

        shader.setUniform("model", new Matrix4f().identity());
        shader.setUniform("view", camera.getViewMatrix());
        shader.setUniform("projection", camera.getProjectionMatrix());

        if (wireframe) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        } else {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }

        world.render();

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
        world.destroy();
    }
}
