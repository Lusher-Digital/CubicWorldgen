package com.screendead.CubicWorldgen.world;

public class World {
    private final Chunk chunk;

    public World() {
        chunk = new Chunk();
    }

    public void render() {
        chunk.render();
    }

    public void destroy() {
        chunk.destroy();
    }
}
