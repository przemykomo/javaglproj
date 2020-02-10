package enger.voxelgame.common.world;

import org.joml.Vector3f;

import java.io.Serializable;

public class Camera implements Serializable {
    public Vector3f position = new Vector3f(0.0f, 2.0f, 0.0f);
    public Vector3f front = new Vector3f(0.0f, 0.0f, -1.0f);
    public Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
    public Vector3f right = new Vector3f(1.0f, 0.0f, 0.0f);

    public static float speed = 0.02f;
    public static float strafeSpeed = 0.01f;
}
