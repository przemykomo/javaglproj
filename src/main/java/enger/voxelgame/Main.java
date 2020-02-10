package enger.voxelgame;

import enger.voxelgame.client.Client;
import enger.voxelgame.server.Server;

public final class Main {
    private static String side;

    public static String getSide() {
        return side;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Pass (side?) \"server\" or \"client (local server?) true/false\" as arguments");
            return;
        }

        if (args[0].equals("client")) {
            if (args.length < 2) {
                System.out.println("Pass \"(local server?) true/false\" as argument");
                return;
            }

            side = "client";

            if (args[1].equals("true")) {
                Server.newInstance(true).launchServer();
            }

            //launch client
            Client.getInstance().launchClient();

        } else if (args[0].equals("server")) {
            //launch server
            side = "server";

            Server.newInstance(false).launchServer();
        }
    }
}
