package com.screendead.CubicWorldgen.world;

import com.screendead.CubicWorldgen.graphics.Mesh;
import org.joml.Vector3f;
import org.lwjgl.stb.STBPerlin;

import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class Chunk {
    public static final int SIZE = 16;
    private static final int HEIGHT = 256;
//    private static final Noise generator = new PerlinNoise(0.1f);

    public final int cx, cz;
    private final int[] blocks = new int[SIZE * SIZE * HEIGHT];
    private Mesh mesh;

    private Chunk negativeX, positiveX, negativeZ, positiveZ;

    public Chunk(int cx, int cz) {
        this.cx = cx;
        this.cz = cz;

        Arrays.fill(blocks, BlockType.AIR.ordinal());

        for (int _x = 0; _x < SIZE; _x++) {
            for (int _z = 0; _z < SIZE; _z++) {
                for (int _y = 0; _y < HEIGHT; _y++) {
                    float x = (float) (_x + (cx << 4)) * 0.005f,
                            z = (float) (_z + (cz << 4)) * 0.005f,
                            y = (float) _y * 0.005f;

                    float height = 1 + (32 * STBPerlin.stb_perlin_ridge_noise3(x, y, z, 2.0f, 0.5f, 1, 2));
                    float limit = 192 - (96 * STBPerlin.stb_perlin_ridge_noise3(x + 2000, y - 1000, z + 1000, 2.0f, 0.5f, 0, 2));

                    if (_y < height && _y < limit) {
                        setBlock(_x, _y, _z, BlockType.GRASS);
                    }
                }
            }
        }
    }

    @SuppressWarnings("all") // TODO: buddy. this is not good. 24/01/2023 01:09 AM
    protected boolean buildMesh() {
        if (this.negativeX == null || this.positiveX == null || this.negativeZ == null || this.positiveZ == null) {
            return false;
        }

        ArrayList<Vector3f> vertices = new ArrayList<>();
        ArrayList<Vector3f> normals = new ArrayList<>();
        ArrayList<Vector3f> colors = new ArrayList<>();

        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
                for (int y = 0; y < HEIGHT; y++) {
                    BlockType block = getBlock(x, y, z);

                    if (block == BlockType.AIR) {
                        continue;
                    }

                    if (block == BlockType.UNKNOWN) {
                        // WE SHOULD NEVER GET HERE
                        throw new RuntimeException("Unknown block type at " + x + ", " + y + ", " + z);
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

                    BlockType pY = getBlock(x, y + 1, z); // +Y
                    BlockType nY = getBlock(x, y - 1, z); // -Y
                    BlockType pX = getBlock(x + 1, y, z); // +X
                    BlockType nX = getBlock(x - 1, y, z); // -X
                    BlockType pZ = getBlock(x, y, z + 1); // +Z
                    BlockType nZ = getBlock(x, y, z - 1); // -Z

                    // +Y
                    if (pY == BlockType.AIR) {
                        vertices.add(blockVertices[2]);
                        vertices.add(blockVertices[3]);
                        vertices.add(blockVertices[7]);
                        vertices.add(blockVertices[2]);
                        vertices.add(blockVertices[7]);
                        vertices.add(blockVertices[6]);

                        for (int i = 0; i < 6; i++) {
                            normals.add(new Vector3f(0.0f, 1.0f, 0.0f));
                        }

                        colors.add(blockColors[2]);
                        colors.add(blockColors[3]);
                        colors.add(blockColors[7]);
                        colors.add(blockColors[2]);
                        colors.add(blockColors[7]);
                        colors.add(blockColors[6]);
                    }

                    // -Y
                    if (nY == BlockType.AIR) {
                        vertices.add(blockVertices[0]);
                        vertices.add(blockVertices[1]);
                        vertices.add(blockVertices[5]);
                        vertices.add(blockVertices[0]);
                        vertices.add(blockVertices[5]);
                        vertices.add(blockVertices[4]);

                        for (int i = 0; i < 6; i++) {
                            normals.add(new Vector3f(0.0f, -1.0f, 0.0f));
                        }

                        colors.add(blockColors[0]);
                        colors.add(blockColors[1]);
                        colors.add(blockColors[5]);
                        colors.add(blockColors[0]);
                        colors.add(blockColors[5]);
                        colors.add(blockColors[4]);
                    }

                    // +X
                    if (pX == BlockType.AIR) {
                        vertices.add(blockVertices[1]);
                        vertices.add(blockVertices[6]);
                        vertices.add(blockVertices[5]);
                        vertices.add(blockVertices[1]);
                        vertices.add(blockVertices[2]);
                        vertices.add(blockVertices[6]);

                        for (int i = 0; i < 6; i++) {
                            normals.add(new Vector3f(1.0f, 0.0f, 0.0f));
                        }

                        colors.add(blockColors[1]);
                        colors.add(blockColors[6]);
                        colors.add(blockColors[5]);
                        colors.add(blockColors[1]);
                        colors.add(blockColors[2]);
                        colors.add(blockColors[6]);
                    }

                    // -X
                    if (nX == BlockType.AIR) {
                        vertices.add(blockVertices[0]);
                        vertices.add(blockVertices[4]);
                        vertices.add(blockVertices[7]);
                        vertices.add(blockVertices[0]);
                        vertices.add(blockVertices[7]);
                        vertices.add(blockVertices[3]);

                        for (int i = 0; i < 6; i++) {
                            normals.add(new Vector3f(-1.0f, 0.0f, 0.0f));
                        }

                        colors.add(blockColors[0]);
                        colors.add(blockColors[4]);
                        colors.add(blockColors[7]);
                        colors.add(blockColors[0]);
                        colors.add(blockColors[7]);
                        colors.add(blockColors[3]);
                    }

                    // +Z
                    if (pZ == BlockType.AIR) {
                        vertices.add(blockVertices[4]);
                        vertices.add(blockVertices[5]);
                        vertices.add(blockVertices[6]);
                        vertices.add(blockVertices[4]);
                        vertices.add(blockVertices[6]);
                        vertices.add(blockVertices[7]);

                        for (int i = 0; i < 6; i++) {
                            normals.add(new Vector3f(0.0f, 0.0f, 1.0f));
                        }

                        colors.add(blockColors[4]);
                        colors.add(blockColors[5]);
                        colors.add(blockColors[6]);
                        colors.add(blockColors[4]);
                        colors.add(blockColors[6]);
                        colors.add(blockColors[7]);
                    }

                    // -Z
                    if (nZ == BlockType.AIR) {
                        vertices.add(blockVertices[0]);
                        vertices.add(blockVertices[3]);
                        vertices.add(blockVertices[2]);
                        vertices.add(blockVertices[0]);
                        vertices.add(blockVertices[2]);
                        vertices.add(blockVertices[1]);

                        for (int i = 0; i < 6; i++) {
                            normals.add(new Vector3f(0.0f, 0.0f, -1.0f));
                        }

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

        float[] _vertices    = new float[vertices.size() * 3];
        float[] _normals     = new float[vertices.size() * 3];
        float[] _colors      = new float[vertices.size() * 3];

        for (int i = 0; i < vertices.size(); i++) {
            Vector3f vertex = vertices.get(i);
            Vector3f normal = normals.get(i);
            Vector3f color = colors.get(i);

            _vertices[i * 3] = vertex.x + cx * SIZE;
            _vertices[i * 3 + 1] = vertex.y;
            _vertices[i * 3 + 2] = vertex.z + cz * SIZE;

            _normals[i * 3] = normal.x;
            _normals[i * 3 + 1] = normal.y;
            _normals[i * 3 + 2] = normal.z;

            _colors[i * 3] = color.x;
            _colors[i * 3 + 1] = color.y;
            _colors[i * 3 + 2] = color.z;
        }

        mesh = new Mesh(GL_STATIC_DRAW, _vertices, _normals, _colors);

        return true;
    }

    public BlockType getBlock(int x, int y, int z) { // X, Y, Z in chunk space
        if (y < 0)  return BlockType.AIR;

        // If the block is outside the chunk, get it from the neighbour chunk
        if (x < 0) {
            if (negativeX == null) {
                return BlockType.UNKNOWN;
            }

            return negativeX.getBlock(SIZE - 1, y, z);
        } else if (x >= SIZE) {
            if (positiveX == null) {
                return BlockType.UNKNOWN;
            }

            return positiveX.getBlock(0, y, z);
        } else if (z < 0) {
            if (negativeZ == null) {
                return BlockType.UNKNOWN;
            }

            return negativeZ.getBlock(x, y, SIZE - 1);
        } else if (z >= SIZE) {
            if (positiveZ == null) {
                return BlockType.UNKNOWN;
            }

            return positiveZ.getBlock(x, y, 0);
        }
        
        return BlockType.values()[blocks[flatten(x, y, z)]];
    }

    public void setBlock(int x, int y, int z, BlockType block) {
        blocks[flatten(x, y, z)] = block.ordinal();
    }

    public void render() {
        if (mesh == null) {
            // This is probably at the edge of the world
            return;
        }

        mesh.render();
    }

    public void destroy() {
        if (mesh == null) {
            return;
        }

        mesh.destroy();
    }

    public static int flatten(int x, int y, int z) {
        return y << 8 | z << 4 | x;
    }

    public void setNeighbours(Chunk negativeX, Chunk positiveX, Chunk negativeZ, Chunk positiveZ) {
        this.negativeX = negativeX;
        this.positiveX = positiveX;
        this.negativeZ = negativeZ;
        this.positiveZ = positiveZ;
    }

    @SuppressWarnings("unused")
    public void setNegativeX(Chunk negativeX) {
        this.negativeX = negativeX;
    }

    @SuppressWarnings("unused")
    public void setPositiveX(Chunk positiveX) {
        this.positiveX = positiveX;
    }

    @SuppressWarnings("unused")
    public void setNegativeZ(Chunk negativeZ) {
        this.negativeZ = negativeZ;
    }

    @SuppressWarnings("unused")
    public void setPositiveZ(Chunk positiveZ) {
        this.positiveZ = positiveZ;
    }
}
