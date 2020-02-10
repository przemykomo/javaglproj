package enger.voxelgame.server.world;

import enger.voxelgame.common.Direction;
import enger.voxelgame.common.world.Camera;
import enger.voxelgame.server.Server;
import enger.voxelgame.util.Tickable;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class ServerCamera extends Camera implements Tickable {

    private transient final Map<Direction, Boolean> movement = new HashMap<>();
    public final String nickname;

    public ServerCamera(String nicknameIn) {
        nickname = nicknameIn;

        for (Direction direction : Direction.values()) {
            movement.put(direction, false);
        }

        Server.getInstance().TICKER.register(this);
    }

    public void startMovingCamera(Direction direction) {
        movement.put(direction, true);
    }

    public void stopMovingCamera(Direction direction) {
        movement.put(direction, false);
    }

    @Override
    public void tick() {
        long deltaTime = Server.getInstance().TICKER.getDeltaTime();
        boolean movementOccurred = false;

        if (movement.get(Direction.FORWARD) && !movement.get(Direction.BACKWARD)) {
            position.add(new Vector3f(front.x, 0, front.z).normalize().mul(speed).mul(deltaTime));
            movementOccurred = true;
        }

        if (movement.get(Direction.BACKWARD) && !movement.get(Direction.FORWARD)) {
            position.sub(new Vector3f(front.x, 0, front.z).normalize().mul(speed).mul(deltaTime));
            movementOccurred = true;
        }

        if (movement.get(Direction.LEFT) && !movement.get(Direction.RIGHT)) {
            position.sub(new Vector3f(right).mul(strafeSpeed).mul(deltaTime));
            movementOccurred = true;
        }

        if (movement.get(Direction.RIGHT) && !movement.get(Direction.LEFT)) {
            position.add(new Vector3f(right).mul(strafeSpeed).mul(deltaTime));
            movementOccurred = true;
        }

        if (movement.get(Direction.UP) && !movement.get(Direction.DOWN)) {
            position.add(new Vector3f(up).mul(speed).mul(deltaTime));
            movementOccurred = true;
        }

        if (movement.get(Direction.DOWN) && !movement.get(Direction.UP)) {
            position.sub(new Vector3f(up).mul(speed).mul(deltaTime));
            movementOccurred = true;
        }

        if (movementOccurred) {
            Server.getInstance().NETWORK.updateCamera(this);
        }
    }

    /**
     * Rotate camera.
     * @param x X mouse movement
     * @param y Y mouse movement
     */
    public void rotate(float x, float y) {
        front.rotateY(-x / 1000);
        front.rotateAxis(-y / 1000, right.x, right.y, right.z);
        calculateRightVector();
    }

    private void calculateRightVector() {
        up.cross(front, right).normalize().negate();
    }
}
