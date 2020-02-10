package enger.voxelgame.common.world;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class World {
    protected Map<Dimension, Chunk> chunkMap = new HashMap<>();

    public Chunk getChunk(int x, int y) {
        return chunkMap.get(new Dimension(x, y));
    }

    public void setChunk(int x, int y, Chunk chunk) {
        chunkMap.put(new Dimension(0, 0), chunk);
    }
}
