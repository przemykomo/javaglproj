package enger.voxelgame.client;

import enger.voxelgame.Main;
import enger.voxelgame.client.net.ClientNet;
import enger.voxelgame.client.render.Window;
import enger.voxelgame.client.world.ClientWorld;
import enger.voxelgame.common.world.World;
import enger.voxelgame.server.Server;
import enger.voxelgame.util.Ticker;

import java.util.Random;

public final class Client {
    private volatile static Client instance;

    public static Client getInstance() {
        if (!Main.getSide().equals("client")) {
            throw new RuntimeException("Requested instance of client while program launched as different side.");
        }
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    public final Window WINDOW;
    public final Ticker TICKER;
    public final ClientNet NETWORK;

    public final ClientWorld WORLD;

    public final String NICKNAME;

    //You can't make new Clients outside of this class
    private Client() {
        WINDOW = new Window("Some title", 800, 600);
        TICKER = new Ticker();
        NETWORK = new ClientNet();

        WORLD = new ClientWorld();

        NICKNAME = Integer.toString(new Random().nextInt());
    }

    private boolean launched;

    public void launchClient() {
        if (launched) {
            throw new RuntimeException("launchClient(): Client is already launched.");
        }

        new Thread(WINDOW, "Client-Window").start();
        new Thread(TICKER, "Client-Ticker").start();
        new Thread(NETWORK, "Client-Network").start();

        launched = true;
    }

    public void exit() {
        TICKER.terminate();
        NETWORK.disconnect();

        if (Server.getInstance() != null && Server.getInstance().isIntegrated()) {
            Server.getInstance().stop();
        }

        System.exit(0);
    }
}