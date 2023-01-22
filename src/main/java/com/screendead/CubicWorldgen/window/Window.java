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
    private final Input input;
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
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);

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

        input = new Input(this);

        float aspectRatio = (float) windowSize.width / (float) windowSize.height;
        renderer = new Renderer(aspectRatio);

        glfwShowWindow(window);
    }

    public void loop() {
        final int UPS = 60;
        final int FPS = 60;

        long initialTime = System.nanoTime();
        final float timeU = 1000000000.0f / UPS;
        final float timeF = 1000000000.0f / FPS;
        float deltaU = 0, deltaF = 0;
        int frames = 0, ticks = 0, totalTicks = 0;
        long timer = System.currentTimeMillis();

        while (!shouldClose()) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - initialTime) / timeU;
            deltaF += (currentTime - initialTime) / timeF;
            initialTime = currentTime;

            if (deltaU >= 1) {
                glfwPollEvents();
                ticks++;
                totalTicks++;

                // Update

                deltaU--;
            }

            if (deltaF >= 1) {
                update();
                renderer.render();
                glfwSwapBuffers(window);

                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                System.out.printf("UPS: %s, FPS: %s, TotalTicks: %s%n", ticks, frames, totalTicks);
                frames = 0;
                ticks = 0;
                timer += 1000;
            }
        }
    }

    public void update() {
        if (input.isKeyPressed(GLFW_KEY_W)) {
            renderer.moveCamera(0, 0, 1);
        }
        if (input.isKeyPressed(GLFW_KEY_S)) {
            renderer.moveCamera(0, 0, -1);
        }
        if (input.isKeyPressed(GLFW_KEY_A)) {
            renderer.moveCamera(-1, 0, 0);
        }
        if (input.isKeyPressed(GLFW_KEY_D)) {
            renderer.moveCamera(1, 0, 0);
        }
        if (input.isKeyPressed(GLFW_KEY_SPACE)) {
            renderer.moveCamera(0, 1, 0);
        }
        if (input.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            renderer.moveCamera(0, -1, 0);
        }
        renderer.wireframe = input.isKeyPressed(GLFW_KEY_PERIOD);

        renderer.rotateCamera(input.getMouseDX(), input.getMouseDY());

        input.update();

        renderer.update();
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

    public long getHandle() {
        return window;
    }

    public float getWidth() {
        return getWindowSize().width;
    }

    public float getHeight() {
        return getWindowSize().height;
    }
}
