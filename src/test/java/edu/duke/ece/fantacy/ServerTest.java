package edu.duke.ece.fantacy;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServerTest {
    //@Test
    void buildServerTest() throws IOException{
        Server server = new Server(1234,5678);
        buildClientTest();
        server.startOnePlayer();
    }

    void buildClientTest(){
        new Thread(()->{
            TCPCommunicator TCPcm = new TCPCommunicator("0.0.0.0", 1234);
//            //System.out.println("Received id is " + cm.receive());
//            TCPcm.sendString("{'position':{'x':'100.00','y':'100.00'}}");
//            System.out.println("Received virtual attribute is " + TCPcm.receive());

            InetAddress address= null;
            try {
                address = InetAddress.getByName("localhost");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            int port=5678;
            String msg = "{'position':{'x':'100.00','y':'100.00'}}";
            byte[] data= msg.getBytes();
            DatagramPacket packet=new DatagramPacket(data, data.length, address, port);
            DatagramSocket socket= null;
            try {
                socket = new DatagramSocket();
            } catch (SocketException e) {
                e.printStackTrace();
            }

            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("[DEBUG] client send message: " + msg);

            byte[] data2=new byte[1024];
            DatagramPacket packet2=new DatagramPacket(data2, data2.length);
            try {
                socket.receive(packet2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String reply=new String(data2, 0, packet2.getLength());
            System.out.println("[DEBUG] client receive message: "+reply);
            socket.close();
        }).start();
    }
}
