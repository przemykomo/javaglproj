package enger.javagl;

import enger.javagl.render.Window;

public class Main  {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public static void main(String[] args) {
        Thread threadGUI = new Thread(new Window("Some title", WIDTH, HEIGHT));
        threadGUI.start();
    }
}
