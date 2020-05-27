package edu.duke.ece.fantacy;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantacy.json.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;

public class TCPcommunicatorTest {
    @Test
    void TCPserverTest() throws IOException {
        ServerSocket TCPserverSock =  new ServerSocket(1111);
        TCPclientTest();
        TCPCommunicator TCPcm = new TCPCommunicator(TCPserverSock);
        ObjectMapper om = new ObjectMapper();

        MessagesS2C server_msg = new MessagesS2C();
        SignUpResultMessage signup_msg = new SignUpResultMessage();
        server_msg.setSignUpResultMessage(signup_msg);

        TCPcm.send(server_msg);
        System.out.println("TCPserver first send: " + om.writeValueAsString(server_msg));

        MessagesS2C server_msg2 = new MessagesS2C();
        LoginResultMessage login_msg = new LoginResultMessage();
        server_msg2.setLoginResultMessage(login_msg);

        TCPcm.send(server_msg2);
        System.out.println("TCPserver second send: " + om.writeValueAsString(server_msg2));
        //TCPcm.close();
        //while(true){}
    }

    void TCPclientTest(){
        new Thread(()->{
            TCPCommunicator TCPcm = new TCPCommunicator("0.0.0.0", 1111);
            ObjectMapper om = new ObjectMapper();
            om.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET,false);
            om.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE,false);
            try {
                System.out.println("waiting to receive");
                InputStream in = TCPcm.socket.getInputStream();

                MessagesS2C result = om.readValue(in, MessagesS2C.class);
                System.out.println("TCPclient first receive: " + om.writeValueAsString(result));

                MessagesS2C result2 = om.readValue(in, MessagesS2C.class);
                System.out.println("TCPclient second receive: " + om.writeValueAsString(result2));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("client failed to receive data");
            }
        }).start();
    }
}
