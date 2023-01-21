package com.screendead.CubicWorldgen;

import com.screendead.CubicWorldgen.window.Window;

public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        Window window = new Window("Cubic Worldgen", 640, 480, false, false);

        window.loop();

        window.destroy();
    }
}
