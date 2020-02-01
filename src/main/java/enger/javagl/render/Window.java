package enger.javagl.render;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Wrapper around {@link Frame}. Sets up window and OpenGL canvas.
 */
public class Window implements Runnable {

    private final Frame frame;
    private final GLCanvas canvas;
    private boolean running = true;

    public Window(String title, int width, int height) {
        GLProfile glp = GLProfile.get(GLProfile.GL4ES3);
        GLCapabilities caps = new GLCapabilities(glp);
        canvas = new GLCanvas(caps);

        frame = new Frame(title);
        frame.setSize(width, height);
        frame.add(canvas);
    }

    @Override
    public void run() {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                running = false;
            }
        });

        canvas.addGLEventListener(new Renderer());

        frame.setVisible(true);

        while (running) {
            canvas.repaint();

            try {
                Thread.sleep(1000 / 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        frame.dispose();
    }

}
