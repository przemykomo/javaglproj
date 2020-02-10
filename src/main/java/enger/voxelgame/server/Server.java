package enger.voxelgame.server;

import enger.voxelgame.server.net.ServerNet;
import enger.voxelgame.server.world.ServerWorld;
import enger.voxelgame.util.Tickable;
import enger.voxelgame.util.Ticker;

public final class Server {
    private static Server instance;

    public static Server newInstance(boolean isLocal) {
        if (instance == null) {
            instance = new Server(isLocal);
        }

        return instance;
    }

    public static Server getInstance() {
        return instance;
    }

    private final boolean isIntegrated;

    /**
     * Is server integrated with client.
     * @return is server integrated
     */
    public boolean isIntegrated() {
        return isIntegrated;
    }

    public final Ticker TICKER;
    public final ServerNet NETWORK;

    public final ServerWorld WORLD;

    //You can't make new Servers outside of this class
    private Server(boolean isIntegratedIn) {
        isIntegrated = isIntegratedIn;
        TICKER = new Ticker();
        if (isIntegratedIn) {
            NETWORK = new ServerNet(1);
        } else {
            NETWORK = new ServerNet(4);
        }

        WORLD = new ServerWorld();
    }

    private boolean launched;

    public void launchServer() {
        if (launched) {
            throw new RuntimeException("launchServer(): Server is already launched.");
        }

        new Thread(TICKER, "Server-ticker").start();
        new Thread(NETWORK, "Server-network").start();

        launched = true;
    }

    public void stop() {
        TICKER.terminate();
        NETWORK.terminate();

        System.exit(0);
    }
}
