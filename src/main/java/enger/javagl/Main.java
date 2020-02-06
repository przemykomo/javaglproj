package enger.javagl;

import enger.javagl.gameplay.Tick;
import enger.javagl.render.Window;

public class Main  {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public static Thread threadGUI;
    public static Thread threadTick;

    public static final Tick TICK = new Tick();
    public static final Window WINDOW = new Window("Some title", WIDTH, HEIGHT);

    /**
     * Main method. Used to create threads.
     * @param args program arguments
     */
    public static void main(String[] args) {
        threadGUI = new Thread(WINDOW, "Thread-window");
        threadTick = new Thread(TICK, "Thread-tick");
        threadGUI.start();
        threadTick.start();
    }
}
