package enger.voxelgame.server.world;

import enger.voxelgame.common.world.Chunk;
import enger.voxelgame.common.world.World;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ServerWorld extends World {

    public Map<String, ServerCamera> cameraMap = new HashMap<>();

    public ServerWorld() {
        chunkMap.put(new Dimension(0, 0), new Chunk());
    }
}
