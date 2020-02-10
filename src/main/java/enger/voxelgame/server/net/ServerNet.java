package enger.voxelgame.server.net;

import enger.voxelgame.common.Direction;
import enger.voxelgame.common.world.Camera;
import enger.voxelgame.server.Server;
import enger.voxelgame.server.world.ServerCamera;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerNet implements Runnable {

    private final ArrayList<ClientHandler> clients = new ArrayList<>();
    private final ExecutorService executorService;
    private volatile ServerSocket serverSocket;

    private volatile boolean running;

    public ServerNet(int maxPlayers) {
        executorService = Executors.newFixedThreadPool(maxPlayers);
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(2553);
            running = true;

            do {
                System.out.println("Waiting for client...");
                Socket socket = serverSocket.accept();
                System.out.println("New client connected.");
                ClientHandler client = new ClientHandler(socket);
                clients.add(client);

                new Thread(client).start();
            } while (!Server.getInstance().isIntegrated() && running);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playerLeft(ClientHandler client) {
        client.disconnect();
        clients.remove(client);

        if (Server.getInstance().isIntegrated()) {
            terminate();
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Server.getInstance().stop();
        }
    }

    public void terminate() {
        running = false;
    }

    private static class ClientHandler implements Runnable {

        private Socket socket;
        private final DataInputStream in;
        private final ObjectInputStream objectIn;
        private final DataOutputStream out;
        private final ObjectOutputStream objectOut;

        private volatile boolean connected;

        private volatile String nickname;

        public String getNickname() {
            return nickname;
        }

        public ClientHandler(Socket socketIn) throws IOException {
            socket = socketIn;
            in = new DataInputStream(socket.getInputStream());
            objectIn = new ObjectInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            objectOut = new ObjectOutputStream(socket.getOutputStream());
            connected = true;
        }

        @Override
        public void run() {
            try {
                while (connected) {
                    try {
                        String msg = in.readUTF();
                        System.out.println("Client says: " + msg);

                        switch (msg) {
                            case "nickname":
                                nickname = in.readUTF();
                                Server.getInstance().WORLD.cameraMap.put(nickname, new ServerCamera(nickname));
                                break;
                            case "getChunk":
                                out.writeUTF("chunkData");
                                objectOut.writeObject(Server.getInstance().WORLD.getChunk(0, 0));
                                break;
                            case "startMovingCamera":
                                Server.getInstance().WORLD.cameraMap.get(nickname).startMovingCamera((Direction) objectIn.readObject());
                                break;
                            case "stopMovingCamera":
                                Server.getInstance().WORLD.cameraMap.get(nickname).stopMovingCamera((Direction) objectIn.readObject());
                            case "rotateCamera":
                                float x = in.readFloat();
                                float y = in.readFloat();
                                Server.getInstance().WORLD.cameraMap.get(nickname).rotate(x, y);
                        }

                    } catch (EOFException ignored) {
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Server.getInstance().NETWORK.playerLeft(this);
        }

        public void disconnect() {
            connected = false;
        }
    }

    public void updateCamera(ServerCamera camera) {
        for (ClientHandler client : clients) {
            if (client.nickname.equals(camera.nickname)) {
                try {
                    client.out.writeUTF("updateCamera");
                    client.objectOut.writeObject(camera);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
