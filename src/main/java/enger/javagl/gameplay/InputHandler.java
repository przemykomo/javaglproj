package enger.javagl.gameplay;

import enger.javagl.render.Window;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'w') {
            Camera.position.add(Camera.front);
            Window.RENDERER.updateCamera = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
