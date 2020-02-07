package enger.javagl.client;

import enger.javagl.client.gameplay.Tick;
import enger.javagl.client.render.Window;

public class Client {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public static Thread threadGUI;
    public static Thread threadTick;

    public static final Tick TICK = new Tick();
    public static final Window WINDOW = new Window("Some title", WIDTH, HEIGHT);

    public static void runClient() {
        threadGUI = new Thread(WINDOW, "Thread-window");
        threadTick = new Thread(TICK, "Thread-tick");
        threadGUI.start();
        threadTick.start();
    }
}
