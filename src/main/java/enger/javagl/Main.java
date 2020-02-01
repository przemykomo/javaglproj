package enger.javagl;

import enger.javagl.render.Window;

public class Main  {

    public static void main(String[] args) {
        Thread threadGUI = new Thread(new Window("Some title", 800, 600));
        threadGUI.start();
    }
}
