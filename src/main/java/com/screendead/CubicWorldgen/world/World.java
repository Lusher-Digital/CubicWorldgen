package com.screendead.CubicWorldgen.world;

import com.screendead.CubicWorldgen.graphics.lighting.Light;
import com.screendead.CubicWorldgen.graphics.lighting.PointLight;
import org.joml.Vector3f;

import java.util.ArrayList;

public class World {
    private final Chunk chunk;

    public World() {
        chunk = new Chunk();

        ArrayList<Light> lights = new ArrayList<>();
        lights.add(new PointLight(
                new Vector3f(-4.0f, 8.0f, -4.0f),
                1.0f,
                0.09f,
                0.032f,
                10.0f));

        chunk.buildMesh(lights);
    }

    public void render() {
        chunk.render();
    }

    public void destroy() {
        chunk.destroy();
    }
}
