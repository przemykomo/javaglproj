package enger.javagl.util;

import java.util.HashSet;
import java.util.Set;

public class Tick implements Runnable {

    private static Set<Tickable> tickables = new HashSet<>();
    private long deltaTimeMillis = 10;
    private volatile boolean running = true;

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {
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
            deltaTimeMillis = (short) (currentTick - lastTick);
            lastTick = currentTick;
        }
    }

    public static void register(Tickable tickable) {
        tickables.add(tickable);
    }

    public static void unregister(Tickable tickable) {
        tickables.remove(tickable);
    }

    public long getDeltaTime() {
        return deltaTimeMillis;
    }
}
