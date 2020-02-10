package enger.voxelgame.util;

import java.util.HashSet;
import java.util.Set;

public class Ticker implements Runnable {

    private volatile Set<Tickable> tickables = new HashSet<>();
    private long deltaTimeMillis = 10;
    private volatile boolean running;

    @Override
    public void run() {
        running = true;

        long lastTick = System.currentTimeMillis();
        long currentTick;

        while (running) {
            for (Tickable tickable : tickables) {
                tickable.tick();
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            currentTick = System.currentTimeMillis();
            deltaTimeMillis = currentTick - lastTick;
            lastTick = currentTick;
        }
    }

    public void terminate() {
        running = false;
    }

    public void register(Tickable tickable) {
        tickables.add(tickable);
    }

    public void unregister(Tickable tickable) {
        tickables.remove(tickable);
    }

    public long getDeltaTime() {
        return deltaTimeMillis;
    }
}
