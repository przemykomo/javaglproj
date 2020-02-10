package enger.voxelgame.client.render;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import enger.voxelgame.client.Client;
import enger.voxelgame.client.InputHandler;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class Window implements Runnable {

    public final Renderer RENDERER;
    public final InputHandler INPUT_HANDLER;

    private final Frame frame;
    private final GLCanvas canvas;

    public Window(String title, int width, int height) {
        RENDERER = new Renderer();
        INPUT_HANDLER = new InputHandler();

        GLProfile glProfile = GLProfile.get(GLProfile.GL4ES3);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);

        canvas = new GLCanvas(glCapabilities);

        frame = new Frame(title);
        frame.setSize(width, height);
        frame.add(canvas);
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();

                Client.getInstance().exit();
            }
        });

        canvas.addGLEventListener(RENDERER);
        canvas.addKeyListener(INPUT_HANDLER);
        canvas.addMouseMotionListener(INPUT_HANDLER);

        frame.setVisible(true);
        frame.setCursor(frame.getToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(), null));

        while (frame.isDisplayable()) {
            canvas.repaint();

            try {
                Thread.sleep(1000 / 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Point getWindowPos() {
        return new Point(frame.getX(), frame.getY());
    }

    public Point getWindowSize() {
        return new Point(frame.getWidth(), frame.getHeight());
    }
}
