package enger.voxelgame.client;

import enger.voxelgame.client.net.ClientNet;
import enger.voxelgame.common.Direction;
import enger.voxelgame.common.world.Camera;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class InputHandler implements KeyListener, MouseMotionListener {

    private Robot robot;

    public InputHandler() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'E') {
            Client.getInstance().WINDOW.RENDERER.updateWireframe();
        }

        ClientNet network = Client.getInstance().NETWORK;

        if (e.getKeyChar() == 'w') {
            network.startMovingCamera(Direction.FORWARD);
        }
        if (e.getKeyChar() == 's') {
            network.startMovingCamera(Direction.BACKWARD);
        }
        if (e.getKeyChar() == 'a') {
            network.startMovingCamera(Direction.LEFT);
        }
        if (e.getKeyChar() == 'd') {
            network.startMovingCamera(Direction.RIGHT);
        }

        if (KeyEvent.getKeyText(e.getKeyCode()).equals("Space")) {
            network.startMovingCamera(Direction.UP);
        }
        if (KeyEvent.getKeyText(e.getKeyCode()).equals("Shift")) {
            network.startMovingCamera(Direction.DOWN);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        ClientNet network = Client.getInstance().NETWORK;

        if (e.getKeyChar() == 'w') {
            network.stopMovingCamera(Direction.FORWARD);
        }
        if (e.getKeyChar() == 's') {
            network.stopMovingCamera(Direction.BACKWARD);
        }
        if (e.getKeyChar() == 'a') {
            network.stopMovingCamera(Direction.LEFT);
        }
        if (e.getKeyChar() == 'd') {
            network.stopMovingCamera(Direction.RIGHT);
        }

        if (KeyEvent.getKeyText(e.getKeyCode()).equals("Space")) {
            network.stopMovingCamera(Direction.UP);
        }
        if (KeyEvent.getKeyText(e.getKeyCode()).equals("Shift")) {
            network.stopMovingCamera(Direction.DOWN);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point windowPos = Client.getInstance().WINDOW.getWindowPos();
        Point windowSize = Client.getInstance().WINDOW.getWindowSize();

        float x = e.getX() - (float) windowSize.x / 2;
        float y = e.getY() - (float) windowSize.y / 2;

        if (x != 0 || y != 0) {
            Client.getInstance().NETWORK.rotateCamera(x, y);

            robot.mouseMove(windowPos.x + windowSize.x / 2 + 4, windowPos.y + windowSize.y / 2 + 20);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
