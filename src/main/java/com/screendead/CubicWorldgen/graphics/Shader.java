package com.screendead.CubicWorldgen.graphics;

import org.joml.*;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

@SuppressWarnings("unused")
public class Shader {
    private final int program;
    private final HashMap<String, Integer> uniforms;

    public Shader(String vertexShader, String fragmentShader) {
        program = glCreateProgram();
        uniforms = new HashMap<>();

        int vertex = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertex, loadShader(vertexShader));
        glCompileShader(vertex);
        if (glGetShaderi(vertex, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Vertex shader failed to compile!");
            System.err.println(glGetShaderInfoLog(vertex));
            System.exit(-1);
        }

        int fragment = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragment, loadShader(fragmentShader));
        glCompileShader(fragment);
        if (glGetShaderi(fragment, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Fragment shader failed to compile!");
            System.err.println(glGetShaderInfoLog(fragment));
            System.exit(-1);
        }

        glAttachShader(program, vertex);
        glAttachShader(program, fragment);
        glLinkProgram(program);
        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println("Shader program failed to link!");
            System.err.println(glGetProgramInfoLog(program));
            System.exit(-1);
        }

        // Set locations for data to be sent to the vertex shader
        glBindAttribLocation(program, 0, "position");
        glBindAttribLocation(program, 1, "color");
        glBindAttribLocation(program, 2, "lightLevel");
        glBindAttribLocation(program, 3, "lightColor");

        glBindVertexArray(glGenVertexArrays());

        glValidateProgram(program);
        if (glGetProgrami(program, GL_VALIDATE_STATUS) == GL_FALSE) {
            System.err.println("Shader program failed to validate!");
            System.err.println(glGetProgramInfoLog(program));
            System.exit(-1);
        }

        glDeleteShader(vertex);
        glDeleteShader(fragment);
    }

    /**
     * Loads a shader from a file using a StringBuilder.
     */
    public static String loadShader(String path) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            InputStream stream = Objects.requireNonNull(Shader.class.getResourceAsStream
                    (path));
            int data;
            while ((data = stream.read()) != -1) {
                shaderSource.append((char) data);
            }
            stream.close();
            return shaderSource.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load shader: " + path, e);
        }
    }

    public static void unbind() {
        glUseProgram(0);
    }

    public void bind() {
        glUseProgram(program);
    }

    public void destroy() {
        unbind();
        glDeleteProgram(program);
    }

    public void addUniform(String name) {
        int uniformLocation = glGetUniformLocation(program, name);
        if (uniformLocation == -1) {
            throw new RuntimeException("Could not find uniform variable '" + name + "'!");
        }
        uniforms.put(name, uniformLocation);
    }


    public void setUniform(String name, int value) {
        glUniform1i(uniforms.get(name), value);
    }


    public void setUniform(String name, float value) {
        glUniform1f(uniforms.get(name), value);
    }


    public void setUniform(String name, boolean value) {
        glUniform1i(uniforms.get(name), value ? 1 : 0);
    }


    public void setUniform(String name, Vector2i value) {
        glUniform2i(uniforms.get(name), value.x, value.y);
    }


    public void setUniform(String name, Vector3i value) {
        glUniform3i(uniforms.get(name), value.x, value.y, value.z);
    }


    public void setUniform(String name, Vector4i value) {
        glUniform4i(uniforms.get(name), value.x, value.y, value.z, value.w);
    }


    public void setUniform(String name, Vector2f value) {
        glUniform2f(uniforms.get(name), value.x, value.y);
    }


    public void setUniform(String name, Vector3f value) {
        glUniform3f(uniforms.get(name), value.x, value.y, value.z);
    }


    public void setUniform(String name, Vector4f value) {
        glUniform4f(uniforms.get(name), value.x, value.y, value.z, value.w);
    }


    public void setUniform(String name, Matrix3f value) {
        glUniformMatrix3fv(uniforms.get(name), false, value.get(new float[9]));
    }

    public void setUniform(String name, Matrix4f value) {
        glUniformMatrix4fv(uniforms.get(name), false, value.get(new float[16]));
    }


    public void setUniform(String name, Matrix3x2f value) {
        glUniformMatrix3x2fv(uniforms.get(name), false, value.get(new float[6]));
    }


    public void setUniform(String name, Matrix4x3f value) {
        glUniformMatrix4x3fv(uniforms.get(name), false, value.get(new float[12]));
    }
}
