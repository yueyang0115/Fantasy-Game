package edu.duke.ece.fantasy;

import java.net.*;
import java.io.*;

//UDP communicator for server

public class UDPCommunicator {
    private DatagramSocket socket;
    private int port;
    private InetAddress clientAddress;

    public UDPCommunicator(DatagramSocket serverSocket){
        this.socket = serverSocket;
    }

    public String receive() {
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        try {
            this.socket.receive(packet);
            this.clientAddress = packet.getAddress();
            this.port = packet.getPort();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[DEBUG] UDP communicator failed to receive data");
        }
        String msg = new String(data, 0, packet.getLength());
        return msg;
    }

    public void SendString(String msg){
        byte[] data = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, this.clientAddress, this.port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[DEBUG] UDP communicator failed to send data");
        }
    }
}
