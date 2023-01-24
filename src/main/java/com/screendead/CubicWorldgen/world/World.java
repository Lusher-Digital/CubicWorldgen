package com.screendead.CubicWorldgen.world;

import java.util.ArrayList;

public class World {
    private final ArrayList<Chunk> chunks = new ArrayList<>();

    public World() {
        chunks.add(new Chunk(0, 0));
        chunks.get(0).buildMesh();
    }

    public void render() {
        for (Chunk chunk : chunks) {
            chunk.render();
        }
    }

    public void destroy() {
        for (Chunk chunk : chunks) {
            chunk.destroy();
        }
    }
}
