package edu.duke.ece.fantacy;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private int TCPport;
    private int UDPport;
    private ServerSocket TCPserverSock;
    private DatagramSocket UDPserverSock;
    private ArrayList<PlayerHandler> playerHandlerList;

    public Server(){
        this.TCPport = 1234;
        try {
            this.TCPserverSock = new ServerSocket(this.TCPport);
            System.out.println("Successfully built TCPserver");
        } catch (IOException e) {
            System.out.println("Failed to build TCPserver");
        }
        this.UDPport = 5678;
        try {
            this.UDPserverSock = new DatagramSocket(this.UDPport);
            System.out.println("Successfully built UDPserver");
        } catch (IOException e) {
            System.out.println("Failed to build UDPserver");
        }
        this.playerHandlerList = new ArrayList<>();
    }

    public void startGame() {
        int id = 0;
        while (true) {
            PlayerHandler ph = new PlayerHandler(new TCPCommunicator(TCPserverSock), new UDPCommunicator(UDPserverSock), id);
            playerHandlerList.add(ph);
            ph.start();
            //ph.startPlay();
            id++;
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.startGame();
    }
}
