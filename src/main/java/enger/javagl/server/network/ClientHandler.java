package enger.javagl.server.network;

import enger.javagl.server.Chunk;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private ObjectOutputStream objectOut;

    private volatile boolean connected;

    private ServerNetwork server;

    public void disconnect() {
        connected = false;
    }

    public ClientHandler(Socket socketIn, ServerNetwork serverIn) throws IOException {
        socket = socketIn;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        objectOut = new ObjectOutputStream(socket.getOutputStream());
        connected = true;
        server = serverIn;
    }

    @Override
    public void run() {
        try {
            while (connected) {
                try {
                    String msg = in.readUTF();
                    System.out.println("Client says " + msg);

                    if (msg.equals("getChunk")) {
                        out.writeUTF("chunkData");
                        objectOut.writeObject(Chunk.blocks);
                    }
                } catch (EOFException ignored) {
                    //there is no string to read
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("Server socket closed");
                server.playerLeft();
                in.close();
                out.close();
                objectOut.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
