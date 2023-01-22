package com.screendead.CubicWorldgen.window;

import com.screendead.CubicWorldgen.graphics.Renderer;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.awt.*;
import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private final long window;
    private final Renderer renderer;

    public Window(String title, int width, int height, boolean maximised, boolean fullscreen) {
        // Set up an error callback. The default implementation
        // will print the error message in System.err.
        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err).set());

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_AUTO_ICONIFY, GLFW_FALSE); // The window will be alt-tabbable without iconifying
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, 1);
        glfwWindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        Dimension vidmode = getVideoMode();

        window = glfwCreateWindow(
                fullscreen
                        ? vidmode.width
                        : maximised
                        ? vidmode.width
                        : width,
                fullscreen
                        ? vidmode.height
                        : maximised
                        ? vidmode.width
                        : height,
                title,
                fullscreen
                        ? glfwGetPrimaryMonitor()
                        : NULL,
                NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
        });

        Dimension windowSize = getWindowSize();
        glfwSetWindowPos(
                window,
                (vidmode.width - windowSize.width) / 2,
                (vidmode.height - windowSize.height) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        float aspectRatio = (float) windowSize.width / (float) windowSize.height;
        renderer = new Renderer(aspectRatio);

        glfwShowWindow(window);
    }

    public void loop() {
        while (!shouldClose()) {
            if (glfwGetCurrentContext() != window) {
                new RuntimeException("GLFW context is not current").printStackTrace();
                System.exit(1);
            }

            renderer.render();

            glfwSwapBuffers(window);

            glfwPollEvents();
        }
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public void destroy() {
        renderer.destroy();

        glfwTerminate();

        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
    }

    public Dimension getWindowSize() {
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            return new Dimension(pWidth.get(0), pHeight.get(0));
        }
    }

    public Dimension getVideoMode() {
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        assert vidmode != null;
        return new Dimension(vidmode.width(), vidmode.height());
    }

}
