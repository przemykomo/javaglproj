package enger.javagl.client.gameplay;

import enger.javagl.client.Client;
import enger.javagl.client.render.Window;
import enger.javagl.util.Tick;
import enger.javagl.util.Tickable;
import org.joml.Vector3f;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;

public class InputHandler implements KeyListener, MouseMotionListener, Tickable {

    Map<String, Boolean> pressedKeys = new HashMap<>();
    private Robot robot;

    public InputHandler() {

        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        pressedKeys.put("W", false);
        pressedKeys.put("S", false);
        pressedKeys.put("A", false);
        pressedKeys.put("D", false);
        pressedKeys.put("Space", false);
        pressedKeys.put("Shift", false);

        Tick.register(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        String keyText = KeyEvent.getKeyText(e.getKeyCode());

        if (keyText.equals("E")) {
            Window.RENDERER.updateWireframe();
        }

        if (keyText.equals("Q")) {
            if (RenderChunk.blocks[2][1][2] == RenderChunk.AIR) {
                RenderChunk.blocks[2][1][2] = RenderChunk.TESTBLOCK;
            } else {
                RenderChunk.blocks[2][1][2] = RenderChunk.AIR;
            }

            Window.RENDERER.updateChunk();
        }

        pressedKeys.put(keyText, true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.put(KeyEvent.getKeyText(e.getKeyCode()), false);
    }

    @Override
    public void tick() {
        if (pressedKeys.get("W")) {
            Camera.position.add(new Vector3f(Camera.front.x, 0, Camera.front.z).normalize().mul(Camera.speed).mul(Client.TICK.getDeltaTime()));
            Window.RENDERER.updateCamera();
        }

        if (pressedKeys.get("S")) {
            Camera.position.sub(new Vector3f(Camera.front.x, 0, Camera.front.z).normalize().mul(Camera.speed).mul(Client.TICK.getDeltaTime()));
            Window.RENDERER.updateCamera();
        }

        if (pressedKeys.get("A")) {
            Camera.position.sub(new Vector3f(Camera.right).mul(Camera.strafeSpeed).mul(Client.TICK.getDeltaTime()));
            Window.RENDERER.updateCamera();
        }

        if (pressedKeys.get("D")) {
            Camera.position.add(new Vector3f(Camera.right).mul(Camera.strafeSpeed).mul(Client.TICK.getDeltaTime()));
            Window.RENDERER.updateCamera();
        }

        if (pressedKeys.get("Space")) {
            Camera.position.add(new Vector3f(Camera.up).mul(Camera.speed).mul(Client.TICK.getDeltaTime()));
            Window.RENDERER.updateCamera();
        }

        if (pressedKeys.get("Shift")) {
            Camera.position.sub(new Vector3f(Camera.up).mul(Camera.speed).mul(Client.TICK.getDeltaTime()));
            Window.RENDERER.updateCamera();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point windowCenter = Client.WINDOW.getWindowCenter();
        Point windowPos = Client.WINDOW.getWindowPos();

        float x = e.getX() - windowCenter.x;
        float y = e.getY() - windowCenter.y;

        if (x != 0 || y != 0) {
            Camera.front.rotateY(-x / 1000);
            Camera.front.rotateAxis(-y / 1000, Camera.right.x, Camera.right.y, Camera.right.z);
            Camera.calculateRightVector();
            Window.RENDERER.updateCamera();

            robot.mouseMove(windowPos.x + windowCenter.x + 4, windowPos.y + windowCenter.y + 20);
        }
    }
}
