package enger.javagl.client;

import enger.javagl.util.Tick;
import enger.javagl.client.network.ClientNetwork;
import enger.javagl.client.render.Window;

public class Client {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public static Thread threadGUI;
    public static Thread threadTick;
    public static Thread threadNetwork;

    public static final Tick TICK = new Tick();
    public static final Window WINDOW = new Window("Some title", WIDTH, HEIGHT);
    public static final ClientNetwork NETWORK = new ClientNetwork();

    public static void launchClient() {
        threadGUI = new Thread(WINDOW, "Thread-window");
        threadTick = new Thread(TICK, "Thread-tick");
        threadNetwork = new Thread(NETWORK, "Thread-client-network");

        threadGUI.start();
        threadTick.start();
        threadNetwork.start();
    }
}
