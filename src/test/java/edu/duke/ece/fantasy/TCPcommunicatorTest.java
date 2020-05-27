package edu.duke.ece.fantasy;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.*;

public class TCPcommunicatorTest {
    @Test
    void TCPserverTest() throws IOException {
        ServerSocket TCPserverSock =  new ServerSocket(1111);
        TCPclientTest();
        TCPCommunicator TCPcm = new TCPCommunicator(TCPserverSock);

        String attributeStr = TCPcm.receive();
        System.out.println("TCPserver receive position: " + attributeStr);

        TCPcm.sendString(attributeStr);
        System.out.println("TCPserver send virtual attribute");
        TCPcm.close();
    }

    void TCPclientTest(){
        new Thread(()->{
            TCPCommunicator TCPcm = new TCPCommunicator("0.0.0.0", 1111);
            TCPcm.sendString("{'position':{'x':'100.00','y':'100.00'}}");
            System.out.println("TCPclient send position");
            System.out.println("TCPclient receive virtual position: " + TCPcm.receive());
        }).start();
    }
}
