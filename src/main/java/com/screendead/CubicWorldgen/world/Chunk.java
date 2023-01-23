package com.screendead.CubicWorldgen.world;

import com.screendead.CubicWorldgen.graphics.Mesh;
import org.joml.Vector3f;

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

                    Vector3f color = block.getColor();

                    Vector3f[] blockVertices = new Vector3f[8];
                    blockVertices[0] = new Vector3f(x, y, z);
                    blockVertices[1] = new Vector3f(x + 1, y, z);
                    blockVertices[2] = new Vector3f(x + 1, y + 1, z);
                    blockVertices[3] = new Vector3f(x, y + 1, z);
                    blockVertices[4] = new Vector3f(x, y, z + 1);
                    blockVertices[5] = new Vector3f(x + 1, y, z + 1);
                    blockVertices[6] = new Vector3f(x + 1, y + 1, z + 1);
                    blockVertices[7] = new Vector3f(x, y + 1, z + 1);

                    Vector3f[] blockColors = new Vector3f[8];
                    Arrays.fill(blockColors, color);

                    /* REMEMBER: Culling is enabled and CCW back face is culled. */

                    // +Y
                    if (getBlock(x, y + 1, z) == BlockType.AIR) {
                        vertices.add(blockVertices[2]);
                        vertices.add(blockVertices[3]);
                        vertices.add(blockVertices[7]);
                        vertices.add(blockVertices[2]);
                        vertices.add(blockVertices[7]);
                        vertices.add(blockVertices[6]);

                        colors.add(blockColors[2]);
                        colors.add(blockColors[3]);
                        colors.add(blockColors[7]);
                        colors.add(blockColors[2]);
                        colors.add(blockColors[7]);
                        colors.add(blockColors[6]);
                    }

                    // -Y
                    if (getBlock(x, y - 1, z) == BlockType.AIR) {
                        vertices.add(blockVertices[0]);
                        vertices.add(blockVertices[1]);
                        vertices.add(blockVertices[5]);
                        vertices.add(blockVertices[0]);
                        vertices.add(blockVertices[5]);
                        vertices.add(blockVertices[4]);

                        colors.add(blockColors[0]);
                        colors.add(blockColors[1]);
                        colors.add(blockColors[5]);
                        colors.add(blockColors[0]);
                        colors.add(blockColors[5]);
                        colors.add(blockColors[4]);
                    }

                    // +X
                    if (getBlock(x + 1, y, z) == BlockType.AIR) {
                        vertices.add(blockVertices[1]);
                        vertices.add(blockVertices[6]);
                        vertices.add(blockVertices[5]);
                        vertices.add(blockVertices[1]);
                        vertices.add(blockVertices[2]);
                        vertices.add(blockVertices[6]);

                        colors.add(blockColors[1]);
                        colors.add(blockColors[6]);
                        colors.add(blockColors[5]);
                        colors.add(blockColors[1]);
                        colors.add(blockColors[2]);
                        colors.add(blockColors[6]);
                    }

                    // -X
                    if (getBlock(x - 1, y, z) == BlockType.AIR) {
                        vertices.add(blockVertices[0]);
                        vertices.add(blockVertices[4]);
                        vertices.add(blockVertices[7]);
                        vertices.add(blockVertices[0]);
                        vertices.add(blockVertices[7]);
                        vertices.add(blockVertices[3]);

                        colors.add(blockColors[0]);
                        colors.add(blockColors[4]);
                        colors.add(blockColors[7]);
                        colors.add(blockColors[0]);
                        colors.add(blockColors[7]);
                        colors.add(blockColors[3]);
                    }

                    // +Z
                    if (getBlock(x, y, z + 1) == BlockType.AIR) {
                        vertices.add(blockVertices[4]);
                        vertices.add(blockVertices[5]);
                        vertices.add(blockVertices[6]);
                        vertices.add(blockVertices[4]);
                        vertices.add(blockVertices[6]);
                        vertices.add(blockVertices[7]);

                        colors.add(blockColors[4]);
                        colors.add(blockColors[5]);
                        colors.add(blockColors[6]);
                        colors.add(blockColors[4]);
                        colors.add(blockColors[6]);
                        colors.add(blockColors[7]);
                    }

                    // -Z
                    if (getBlock(x, y, z - 1) == BlockType.AIR) {
                        vertices.add(blockVertices[0]);
                        vertices.add(blockVertices[3]);
                        vertices.add(blockVertices[2]);
                        vertices.add(blockVertices[0]);
                        vertices.add(blockVertices[2]);
                        vertices.add(blockVertices[1]);

                        colors.add(blockColors[0]);
                        colors.add(blockColors[3]);
                        colors.add(blockColors[2]);
                        colors.add(blockColors[0]);
                        colors.add(blockColors[2]);
                        colors.add(blockColors[1]);
                    }
                }
            }
        }

        float[] _vertices = new float[vertices.size() * 3];
        float[] _colors = new float[colors.size() * 3];

        for (int i = 0; i < vertices.size(); i++) {
            Vector3f vertex = vertices.get(i);
            Vector3f color = colors.get(i);

            _vertices[i * 3] = vertex.x;
            _vertices[i * 3 + 1] = vertex.y;
            _vertices[i * 3 + 2] = vertex.z;

            _colors[i * 3] = color.x;
            _colors[i * 3 + 1] = color.y;
            _colors[i * 3 + 2] = color.z;
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
