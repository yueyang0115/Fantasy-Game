package edu.duke.ece.fantacy;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.*;

public class UDPcommunicatorTest {
    @Test
    void UDPserverTest() throws IOException {
        DatagramSocket UDPserverSock = new DatagramSocket(5555);
        UDPclientTest();
        UDPCommunicator UDPcm = new UDPCommunicator(UDPserverSock);

        String attributeStr = UDPcm.receive();
        System.out.println("UDPserver receive attribute: " +attributeStr);

        //transform
        JsonToAttribute jsonToattribute = new JsonToAttribute(attributeStr);
        Attribute attribute = jsonToattribute.getAttribute();
        AttributeToJson attributeToJson = new AttributeToJson(attribute);
        JSONObject attributeObj = attributeToJson.getAttributeObj();

        UDPcm.SendString(attributeObj.toString());
        System.out.println("UDPserver send virtual attribute");
    }

    void UDPclientTest(){
        new Thread(()->{
            InetAddress address= null;
            try {
                address = InetAddress.getByName("localhost");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            int port=5555;
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
            System.out.println("UDPclient send message");

            byte[] data2=new byte[1024];
            DatagramPacket packet2=new DatagramPacket(data2, data2.length);
            try {
                socket.receive(packet2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String reply=new String(data2, 0, packet2.getLength());
            System.out.println("UDPclient receive message: "+reply);
            socket.close();
        }).start();
    }
}
