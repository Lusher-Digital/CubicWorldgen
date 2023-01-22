package com.screendead.CubicWorldgen.graphics;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private final int vao, ibo;
    private final List<Integer> vbos = new ArrayList<>();
    private final int count;

    public Mesh(int mode, float[] vertices, float[] normals, float[] colors, int[] indices) {
        count = indices.length;

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        // Vertices
        int vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, mode);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        // Normals
        vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, normals, mode);
        glVertexAttribPointer(1, 3, GL_FLOAT, true, 0, 0);

        // Colors
        vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, colors, mode);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

        // Indices
        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, mode);
    }

    public void render() {
        // Draw the mesh
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }

    public void destroy() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vbo : vbos) {
            glDeleteBuffers(vbo);
        }

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDeleteBuffers(ibo);

        glBindVertexArray(0);
        glDeleteVertexArrays(vao);
    }

}
