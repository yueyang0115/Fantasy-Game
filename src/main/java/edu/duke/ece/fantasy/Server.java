package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.levelUp.TableInitializer;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class Server {
    private int TCPport;
    private int UDPport;
    private ServerSocket TCPserverSock;
    private DatagramSocket UDPserverSock;
    private ArrayList<PlayerHandler> playerHandlerList;

    public Server(int tcpPort, int udpPort) {
        this.TCPport = tcpPort;
        try {
            this.TCPserverSock = new ServerSocket(this.TCPport);
            System.out.println("[DEBUG] Successfully built TCPserver");
        } catch (IOException e) {
            System.out.println("[DEBUG] Failed to build TCPserver");
            e.printStackTrace();
        }
        this.UDPport = udpPort;
        try {
            this.UDPserverSock = new DatagramSocket(this.UDPport);
            System.out.println("[DEBUG] Successfully built UDPserver");
        } catch (IOException e) {
            System.out.println("[DEBUG] Failed to build UDPserver");
            e.printStackTrace();
        }
        this.playerHandlerList = new ArrayList<>();
    }

    public void startGame() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            // create a jdbc connection for table initialization
            SessionImplementor sessImpl = (SessionImplementor) session;
            Connection connection =  sessImpl.getJdbcConnectionAccess().obtainConnection();

            // initialize SkillTable
            TableInitializer tableInitializer = new TableInitializer(session, connection);
            tableInitializer.initializeAll();
            session.getTransaction().commit();
        }
        catch (SQLException e){
            System.out.println("Failed to create session and jdbc connection");
            e.printStackTrace();
        }

        while (true) {
            // one thread per player
            PlayerHandler ph = new PlayerHandler(new TCPCommunicator(TCPserverSock), new UDPCommunicator(UDPserverSock));
            playerHandlerList.add(ph);
            ph.start();
        }
    }

    public void startOnePlayer() {
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
