package edu.duke.ece.fantacy;

import java.io.IOException;
import java.net.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServerTest {
//    @Test
//    void buildServerTest() {
//        Server server = new Server();
//        server.main(null);
//    }

    //@Test
    void buildClientTest() throws IOException {
        for(int i=0;i<10;i++){
            TCPCommunicator TCPcm = new TCPCommunicator("0.0.0.0", 1234);
//            //System.out.println("Received id is " + cm.receive());
//            TCPcm.sendString("{'position':{'x':'100.00','y':'100.00'}}");
//            System.out.println("Received virtual attribute is " + TCPcm.receive());

            InetAddress address=InetAddress.getByName("localhost");
            int port=5678;
            String msg = "{'position':{'x':'100.00','y':'100.00'}}";
            byte[] data= msg.getBytes();
            DatagramPacket packet=new DatagramPacket(data, data.length, address, port);
            DatagramSocket socket=new DatagramSocket();
            socket.send(packet);
            System.out.println("client send message: " + msg);

            byte[] data2=new byte[1024];
            DatagramPacket packet2=new DatagramPacket(data2, data2.length);
            socket.receive(packet2);
            String reply=new String(data2, 0, packet2.getLength());
            System.out.println("client receive message: "+reply);
            socket.close();
        }
    }
}
