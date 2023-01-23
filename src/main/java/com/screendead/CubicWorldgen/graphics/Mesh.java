package com.screendead.CubicWorldgen.graphics;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private final int vao;
    private final List<Integer> vbos = new ArrayList<>();
    private final int count;

    public Mesh(int mode, float[] vertices, float[] colors) {
        count = vertices.length;

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        // Vertices
        int vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, mode);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        // Colors
        vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, colors, mode);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
    }

    public void render() {
        // Draw the mesh
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawArrays(GL_TRIANGLES, 0, count);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
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

        glBindVertexArray(0);
        glDeleteVertexArrays(vao);
    }
}
