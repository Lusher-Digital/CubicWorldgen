package com.screendead.CubicWorldgen.world;

import java.util.HashMap;

public class World {
    private static final int SIZE = 4;
    private static final int HALF_SIZE = SIZE / 2;

    private final HashMap<String, Chunk> chunks = new HashMap<>();

    public World() {
        for (int x = -HALF_SIZE; x < HALF_SIZE; x++) {
            for (int z = -HALF_SIZE; z < HALF_SIZE; z++) {
                chunks.put(x + "," + z, new Chunk(x, z));
            }
        }
        for (int x = -HALF_SIZE; x < HALF_SIZE; x++) {
            for (int z = -HALF_SIZE; z < HALF_SIZE; z++) {
                if (x == -HALF_SIZE
                        || x == HALF_SIZE - 1
                        || z == -HALF_SIZE
                        || z == HALF_SIZE - 1) {
                    continue;
                }

                chunks.get(x + "," + z).setNeighbours(
                        chunks.get((x - 1) + "," + z),
                        chunks.get((x + 1) + "," + z),
                        chunks.get(x + "," + (z - 1)),
                        chunks.get(x + "," + (z + 1))
                );

                boolean result = chunks.get(x + "," + z).buildMesh();

                if (!result) {
                    System.out.println("Failed to build mesh for chunk at " + x + ", " + z);
                }
            }
        }
    }

    public void render() {
        chunks.values().forEach(Chunk::render);
    }

    @SuppressWarnings("unused")
    public void getBlock(int x, int y, int z) {
        int cx = x / Chunk.SIZE;
        int cz = z / Chunk.SIZE;
        int bx = x % Chunk.SIZE;
        int bz = z % Chunk.SIZE;

        chunks.get(cx + "," + cz).getBlock(bx, y, bz);
    }

    public void destroy() {
        chunks.values().forEach(Chunk::destroy);
    }
}
