package enger.javagl;

import enger.javagl.client.Client;

public class Main  {

    /**
     * Main method. Used to launch game.
     * @param args program arguments. First argument must be either "client" or "server".
     */
    public static void main(String[] args) {
        switch (args[0]) {
            case "client":
                Client.runClient();
                break;
            case "server":
            default:
                System.out.println("Wrong argument or functionality not yet implemented!");
        }
    }
}
