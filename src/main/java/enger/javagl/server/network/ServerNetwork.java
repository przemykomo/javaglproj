package enger.javagl.server.network;

import enger.javagl.server.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerNetwork implements Runnable {

    private ArrayList<ClientHandler> clients = new ArrayList<>();
    private ExecutorService pool;
    private volatile Server server;
    private volatile ServerSocket serverSocket;

    public ServerNetwork(int maxPlayers, Server serverIn) {
        pool = Executors.newFixedThreadPool(maxPlayers);
        server = serverIn;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(2553);
            server.running = true;

            do {
                try {
                    System.out.println("Waiting for a client...");
                    Socket socket = serverSocket.accept();
                    System.out.println("Connected to new client!");
                    ClientHandler client = new ClientHandler(socket, this);
                    clients.add(client);

                    pool.execute(client);
                } catch (SocketException ignored) {}
            } while (server.running && !server.isLocal);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playerLeft() {
        if (server.isLocal) {
            server.running = false;

            for (ClientHandler client : clients) {
                client.disconnect();
            }

            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
