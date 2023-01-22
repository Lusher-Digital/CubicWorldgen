package com.screendead.CubicWorldgen.world;

import com.screendead.CubicWorldgen.graphics.Face;
import com.screendead.CubicWorldgen.graphics.Facing;
import com.screendead.CubicWorldgen.graphics.Mesh;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class Chunk {
    private final int[] blocks;
    private Mesh mesh;

    public Chunk() {
        blocks = new int[65524]; // 16 * 16 * 16

        Arrays.fill(blocks, BlockType.AIR.ordinal());

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 64; y++) {
                    setBlock(x, y, z, BlockType.STONE);
                }
            }
        }

        buildMesh();
    }

    private void buildMesh() {
        ArrayList<Vector3f> vertices = new ArrayList<>();
        ArrayList<Vector3f> colors = new ArrayList<>();

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    BlockType block = getBlock(x, y, z);

                    if (block == BlockType.AIR) {
                        continue;
                    }

                    BlockType[] neighbors = new BlockType[6];
                    neighbors[Facing.NORTH.ordinal()] = getBlock(x, y, z - 1);
                    neighbors[Facing.SOUTH.ordinal()] = getBlock(x, y, z + 1);
                    neighbors[Facing.EAST.ordinal()] = getBlock(x + 1, y, z);
                    neighbors[Facing.WEST.ordinal()] = getBlock(x - 1, y, z);
                    neighbors[Facing.UP.ordinal()] = getBlock(x, y + 1, z);
                    neighbors[Facing.DOWN.ordinal()] = getBlock(x, y - 1, z);

                    for (int i = 0; i < 6; i++) {
                        if (neighbors[i] == BlockType.AIR) {
                            Face face = block.getFace(
                                    new Vector3i(x, y, z),
                                    Facing.values()[i]
                            );
                            vertices.addAll(face.getFace());
                        }
                    }

                    for (int i = 0; i < 6; i++) {
                        if (neighbors[i] == BlockType.AIR) {
                            Vector3f color = block.getColor();

                            for (int j = 0; j < 6; j++) {
                                colors.add(color);
                            }
                        }
                    }
                }
            }
        }

        float[] _vertices = new float[vertices.size() * 3];
        float[] _colors = new float[colors.size() * 3];

        for (int i = 0; i < vertices.size(); i++) {
            _vertices[i * 3] = vertices.get(i).x;
            _vertices[i * 3 + 1] = vertices.get(i).y;
            _vertices[i * 3 + 2] = vertices.get(i).z;
        }

        for (int i = 0; i < colors.size(); i++) {
            _colors[i * 3] = colors.get(i).x;
            _colors[i * 3 + 1] = colors.get(i).y;
            _colors[i * 3 + 2] = colors.get(i).z;
        }

        mesh = new Mesh(GL_STATIC_DRAW, _vertices, _colors);
    }

    public BlockType getBlock(int x, int y, int z) {
        if (x < 0 || x >= 16 || y < 0 || y >= 16 || z < 0 || z >= 16) {
            return BlockType.AIR;
        }

        return BlockType.values()[blocks[flatten(x, y, z)]];
    }

    public void setBlock(int x, int y, int z, BlockType block) {
        blocks[flatten(x, y, z)] = block.ordinal();
    }

    public void render() {
        mesh.render();
    }

    public void destroy() {
        mesh.destroy();
    }

    public static int flatten(int x, int y, int z) {
        return x + (y << 4) + (z << 8);
    }
}
