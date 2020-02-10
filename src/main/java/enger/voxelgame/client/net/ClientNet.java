package enger.voxelgame.client.net;

import enger.voxelgame.client.Client;
import enger.voxelgame.common.Direction;
import enger.voxelgame.common.world.Camera;
import enger.voxelgame.common.world.Chunk;

import java.io.*;
import java.net.Socket;

/**
 * Client-side communication with server-side.
 */
public class ClientNet implements Runnable {

    private volatile DataOutputStream dataOutputStream;
    private volatile ObjectOutputStream objectOutputStream;

    private volatile boolean connected;

    @Override
    public void run() {
        try {
            System.out.println("Connecting to server...");
            Socket socket = new Socket("localhost", );
            connected = true;
            System.out.println("Connected to server");

            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            dataOutputStream.writeUTF("nickname");
            dataOutputStream.writeUTF(Client.getInstance().NICKNAME);

            requestChunkData();

            while (connected) {
                try {
                    String msg = dataInputStream.readUTF();

                    System.out.println("Server says: " + msg);

                    switch (msg) {
                        case "chunkData":
                            Client.getInstance().WORLD.setChunk(0, 0, (Chunk) objectInputStream.readObject());
                            Client.getInstance().WINDOW.RENDERER.updateChunk();
                            break;
                        case "updateCamera":
                            Client.getInstance().WORLD.playerCamera = (Camera) objectInputStream.readObject();
                            Client.getInstance().WINDOW.RENDERER.updateCamera();
                            break;
                    }
                } catch (EOFException ignored) {} catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            dataInputStream.close();
            objectInputStream.close();
            dataOutputStream.close();
            socket.close();
            System.out.println("Client socket closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        connected = false;
    }

    public void requestChunkData() {
        try {
            dataOutputStream.writeUTF("getChunk");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startMovingCamera(Direction direction) {
        try {
            dataOutputStream.writeUTF("startMovingCamera");
            objectOutputStream.writeObject(direction);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopMovingCamera(Direction direction) {
        try {
            dataOutputStream.writeUTF("stopMovingCamera");
            objectOutputStream.writeObject(direction);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rotateCamera(float x, float y) {
        try {
            dataOutputStream.writeUTF("rotateCamera");
            dataOutputStream.writeFloat(x);
            dataOutputStream.writeFloat(y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
