package enger.javagl.gameplay;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Camera {
    public static Vector3f position = new Vector3f();
    public static Vector3f front = new Vector3f(0.0f, 0.0f, -1.0f);
    public static Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
    public static Vector3f right = new Vector3f(1.0f, 0.0f, 0.0f);

    public static void calculateRightVector() {
        up.cross(front, right).normalize().negate();
    }

    public static float speed = 0.02f;
    public static float strafeSpeed = 0.01f;
}
