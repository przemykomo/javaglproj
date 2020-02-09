package enger.javagl.server;

import enger.javagl.util.Tick;
import enger.javagl.server.network.ServerNetwork;

public class Server {

    public Thread threadServer;
    public Thread threadNetwork;

    public final Tick SERVER_TICK = new Tick();
    public final ServerNetwork NETWORK;

    public volatile boolean running;
    public final boolean isLocal;

    public Server(int maxPlayers, boolean isLocalIn) {
        NETWORK = new ServerNetwork(maxPlayers, this);
        isLocal = isLocalIn;
    }

    public void launchServer() throws Exception {
        if (running) throw new Exception("Server is already running!");

        threadServer = new Thread(SERVER_TICK, "Thread-server");
        threadNetwork = new Thread(NETWORK, "Thread-server-network");

        threadServer.start();
        threadNetwork.start();
    }
}
