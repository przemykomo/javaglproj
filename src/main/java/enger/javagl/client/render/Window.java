package enger.javagl.client.render;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import enger.javagl.client.gameplay.InputHandler;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

/**
 * Wrapper around {@link Frame}. Sets up window and OpenGL canvas.
 */
public class Window implements Runnable {

    public static final Renderer RENDERER = new Renderer();
    private static final InputHandler INPUT_HANDLER = new InputHandler();

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

        canvas.addGLEventListener(RENDERER);
        canvas.addKeyListener(INPUT_HANDLER);
        canvas.addMouseMotionListener(INPUT_HANDLER);

        frame.setVisible(true);
        //Set cursor as invisible
        frame.setCursor(frame.getToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(), null));

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

    public Point getWindowPos() {
        return new Point(frame.getX(), frame.getY());
    }

    public Point getWindowCenter() {
        return new Point(frame.getWidth() / 2, frame.getHeight() / 2);
    }
}
