package edu.duke.ece.fantacy;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private int port;
    private ServerSocket serverSock;
    private ArrayList<PlayerHandler> playerHandlerList;

    public Server(){
        this.port = 1234;
        try {
            this.serverSock = new ServerSocket(port);
            System.out.println("Successfully built server");
        } catch (IOException e) {
            System.out.println("Failed to build ServerSocket!");
        }
        this.playerHandlerList = new ArrayList<>();
    }

    public void startGame() {
        int id = 0;
        while (true) {
            PlayerHandler ph = new PlayerHandler(new Communicator(serverSock), id);
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
