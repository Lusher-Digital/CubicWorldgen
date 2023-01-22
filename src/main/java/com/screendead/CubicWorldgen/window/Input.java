package com.screendead.CubicWorldgen.window;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
    private final Window window;
    private float mouseDX = 0, mouseDY = 0;
    private int numberOfMouseMoves = 0;

    public Input(Window window) {
        this.window = window;

        glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        glfwSetKeyCallback(window.getHandle(),
                (window1, key, scancode, action, mods) -> {
                    if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                        glfwSetWindowShouldClose(window1, true);
                });
        glfwSetCursorPosCallback(window.getHandle(),
                (window1, xpos, ypos) -> {
                    // Reset mouse position to center of screen
                    setMousePosition(window.getWidth() / 2, window.getHeight() / 2);

                    numberOfMouseMoves++;
                    if (numberOfMouseMoves < 2) return;

                    // Calculate mouse movement
                    mouseDX += (float) (xpos - window.getWidth() / 2);
                    mouseDY += (float) (ypos - window.getHeight() / 2);
                });
    }

    public boolean isKeyPressed(int key) {
        return glfwGetKey(window.getHandle(), key) == GLFW_PRESS;
    }

    public float getMouseDX() {
        return mouseDX;
    }

    public float getMouseDY() {
        return mouseDY;
    }

    public void setMousePosition(float x, float y) {
        glfwSetCursorPos(window.getHandle(), x, y);
    }

    public void update() {
        mouseDX = 0;
        mouseDY = 0;
    }
}
