package enger.javagl.client.network;

import enger.javagl.client.gameplay.RenderChunk;
import enger.javagl.client.render.Window;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientNetwork implements Runnable {

    private volatile DataOutputStream out;
    private volatile DataInputStream in;
    private volatile ObjectInputStream objectIn;

    private volatile boolean connected = false;

    public void disconnect() {
        connected = false;
    }

    //TODO: argument choosing chunk and use something different from writeUTF
    public void requestChunkData() {
        try {
            out.writeUTF("getChunk");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Connecting to server...");
            Socket socket = new Socket("localhost", 2553);
            connected = true;
            System.out.println("Connected to server");

            out = new DataOutputStream(socket.getOutputStream());
            objectIn = new ObjectInputStream(socket.getInputStream());
            in = new DataInputStream(socket.getInputStream());

            requestChunkData();

            while (connected) {
                try {
                    String msg = in.readUTF();

                    if (msg.equals("chunkData")) {
                        //TODO: get chunk coordinate
                        RenderChunk.blocks = (short[][][]) objectIn.readObject();
                        Window.RENDERER.updateChunk();
                    }
                } catch (EOFException | SocketException ignored) {
                    //there is no string to read or server closed connection
                }
            }

            System.out.println("Client socket closed");
            in.close();
            out.close();
            objectIn.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
