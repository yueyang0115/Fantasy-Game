package edu.duke.ece.fantacy;

import java.net.*;
import java.io.*;
import org.json.*;

public class TCPCommunicator {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public TCPCommunicator(ServerSocket serverSocket) {
        try {
            this.socket = serverSocket.accept();
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Failed to accept player socket!");
        }
    }

    public TCPCommunicator(String ip, int port) {
        try {
            this.socket = new Socket(ip, port);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Failed to crete Communicator!");
        }
    }

    public void sendString(String str) {
        out.println(str);
    }

    public void sendJSON(JSONObject json) {
        sendString(json.toString());
    }

    public String receive() {
        String res = "";
        try {
            res = in.readLine();
        } catch (IOException e) {
            System.out.println("Failed to receive data!");
        }
        return res;
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Failed to close socket!");
        }
    }
}
