package edu.duke.ece.fantacy;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.*;

public class TCPcommunicatorTest {
    @Test
    void TCPserverTest() throws IOException {
        ServerSocket TCPserverSock =  new ServerSocket(1111);
        TCPclientTest();
        TCPCommunicator TCPcm = new TCPCommunicator(TCPserverSock);

        int id = 0;
        String attributeStr = TCPcm.receive();
        System.out.println("TCPserver receive position: " + attributeStr);

        //transform
        JsonToAttribute jsonToattribute = new JsonToAttribute(attributeStr);
        Attribute attribute = jsonToattribute.getAttribute();
        AttributeToJson attributeToJson = new AttributeToJson(attribute);
        JSONObject attributeObj = attributeToJson.getAttributeObj();

        TCPcm.sendString(attributeObj.toString());
        System.out.println("TCPserver send virtual attribute");
        TCPcm.close();
    }

    void TCPclientTest(){
        new Thread(()->{
            TCPCommunicator TCPcm = new TCPCommunicator("0.0.0.0", 1111);
            TCPcm.sendString("{'position':{'x':'100.00','y':'100.00'}}");
            System.out.println("TCPclient send position");
            System.out.println("TCPclient receive virtual attribute: " + TCPcm.receive());
        }).start();
    }
}
