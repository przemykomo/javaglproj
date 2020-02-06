package enger.javagl.gameplay;

import enger.javagl.Main;
import enger.javagl.render.Window;
import org.joml.Vector3f;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;

public class InputHandler implements KeyListener, MouseMotionListener, Tickable {

    Map<Character, Boolean> pressedKeys = new HashMap<>();
    private Robot robot;

    public InputHandler() {

        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        pressedKeys.put('w', false);
        pressedKeys.put('s', false);
        pressedKeys.put('a', false);
        pressedKeys.put('d', false);

        Tick.register(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.put(e.getKeyChar(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.put(e.getKeyChar(), false);
    }

    @Override
    public void tick() {
        if (pressedKeys.get('w')) {
            Camera.position.add(new Vector3f(Camera.front.x, 0, Camera.front.z).normalize().mul(Camera.speed).mul(Main.TICK.getDeltaTime()));
            Window.RENDERER.updateCamera = true;
        }

        if (pressedKeys.get('s')) {
            Camera.position.sub(new Vector3f(Camera.front.x, 0, Camera.front.z).normalize().mul(Camera.speed).mul(Main.TICK.getDeltaTime()));
            Window.RENDERER.updateCamera = true;
        }

        if (pressedKeys.get('a')) {
            Camera.position.sub(new Vector3f(Camera.right).mul(Camera.strafeSpeed).mul(Main.TICK.getDeltaTime()));
            Window.RENDERER.updateCamera = true;
        }

        if (pressedKeys.get('d')) {
            Camera.position.add(new Vector3f(Camera.right).mul(Camera.strafeSpeed).mul(Main.TICK.getDeltaTime()));
            Window.RENDERER.updateCamera = true;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point windowCenter = Main.WINDOW.getWindowCenter();
        Point windowPos = Main.WINDOW.getWindowPos();

        float x = e.getX() - windowCenter.x;
        float y = e.getY() - windowCenter.y;

        if (x != 0 || y != 0) {
            Camera.front.rotateY(-x / 1000);

            Camera.front.rotateAxis(-y / 1000, Camera.right.x, Camera.right.y, Camera.right.z);
//            Camera.front.rotateX(-y / 1000);
            Camera.calculateRightVector();
            Window.RENDERER.updateCamera = true;

            System.out.print(x);
            System.out.print(' ');
            System.out.println(y);

            robot.mouseMove(windowPos.x + windowCenter.x + 4, windowPos.y + windowCenter.y + 20);
        }
    }
}
