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
            while(this.socket ==null){
                this.socket = serverSocket.accept();
            }
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("[DEBUG] TCP communicator successfully accept player socket!");
        } catch (IOException e) {
            System.out.println("[DEBUG] TCP communicator failed to accept player socket!");
        }
    }

    public TCPCommunicator(String ip, int port) {
        try {
            this.socket = new Socket(ip, port);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("[DEBUG] TCP communicator failed to crete TCPCommunicator!");
        }
    }

    public void sendString(String str) {
        String msg = str + "\n";
        out.println(msg);
    }

    public String receive() {
        String res = "";
        try {
            while(in.ready()){
                res = in.readLine();
            }
        } catch (IOException e) {
            System.out.println("[DEBUG] TCP communicator failed to receive data!");
        }
        return res;
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("[DEBUG] TCP communicator failed to close socket!");
        }
    }
}
