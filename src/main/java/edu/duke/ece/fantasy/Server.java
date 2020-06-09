package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.HibernateUtil;
import org.hibernate.Session;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private int TCPport;
    private int UDPport;
    private ServerSocket TCPserverSock;
    private DatagramSocket UDPserverSock;
    private ArrayList<PlayerHandler> playerHandlerList;
    //private MockDBprocessor myMockDBprocessor;
//    private DBprocessor myDBprocessor;

    public Server(int tcpPort, int udpPort) {
        this.TCPport = tcpPort;
        try {
            this.TCPserverSock = new ServerSocket(this.TCPport);
            System.out.println("[DEBUG] Successfully built TCPserver");
        } catch (IOException e) {
            System.out.println("[DEBUG] Failed to build TCPserver");
        }
        this.UDPport = udpPort;
        try {
            this.UDPserverSock = new DatagramSocket(this.UDPport);
            System.out.println("[DEBUG] Successfully built UDPserver");
        } catch (IOException e) {
            System.out.println("[DEBUG] Failed to build UDPserver");
        }
        this.playerHandlerList = new ArrayList<>();
        //this.myMockDBprocessor = new MockDBprocessor();
//        this.myDBprocessor = new DBprocessor();
    }

    public void startGame() {
        (new Initializer()).initialize();
        //myMockDBprocessor.create();
//        myDBprocessor.connectDB();
        while (true) {
            PlayerHandler ph = new PlayerHandler(new TCPCommunicator(TCPserverSock), new UDPCommunicator(UDPserverSock));
            playerHandlerList.add(ph);
            ph.start();
            //ph.startPlay();
        }
    }

    public void startOnePlayer() {
        int id = 0;
        PlayerHandler ph = new PlayerHandler(new TCPCommunicator(TCPserverSock), new UDPCommunicator(UDPserverSock));
        playerHandlerList.add(ph);
        ph.start();
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("close session factory");
            HibernateUtil.shutdown();
        }));
        Server server = new Server(1234, 5678);
        server.startGame();

    }
}
