package enger.javagl;

import enger.javagl.client.Client;
import enger.javagl.server.Server;

public class Main  {

    /**
     * Main method. Used to launch game.
     * @param args program arguments. First argument must be either "client" or "server".
     */
    public static void main(String[] args) throws Exception {
        switch (args[0]) {
            case "client":
                if (args.length > 1 && args[1].equals("true")) {
                    new Server(1, true).launchServer();
                }
                Client.launchClient();
                break;
            case "server":
                new Server(4, false).launchServer();
                break;
            default:
                System.out.println("Wrong argument or functionality not yet implemented!");
        }
    }
}
