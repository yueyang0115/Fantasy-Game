package edu.duke.ece.fantacy;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantacy.json.LoginRequestMessage;
import edu.duke.ece.fantacy.json.LoginResultMessage;
import edu.duke.ece.fantacy.json.MessagesC2S;
import edu.duke.ece.fantacy.json.MessagesS2C;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.*;

public class TCPcommunicatorTest {
    @Test
    void TCPserverTest() throws IOException {
        ServerSocket TCPserverSock =  new ServerSocket(1111);
        TCPclientTest();
        TCPCommunicator TCPcm = new TCPCommunicator(TCPserverSock);

        MessagesS2C server_msg = new MessagesS2C();
        LoginResultMessage login_msg = new LoginResultMessage();
        server_msg.setLoginResultMessage(login_msg);

        TCPcm.send(server_msg);
        ObjectMapper om = new ObjectMapper();

        System.out.println("TCPserver send: " + om.writeValueAsString(server_msg));
        //TCPcm.close();
    }

    void TCPclientTest(){
        new Thread(()->{
            TCPCommunicator TCPcm = new TCPCommunicator("0.0.0.0", 1111);
            ObjectMapper om = new ObjectMapper();
            try {
                System.out.println("waiting to receive");
                MessagesS2C result = om.readValue(TCPcm.socket.getInputStream(), MessagesS2C.class);
                System.out.println("TCPclient receive: " + om.writeValueAsString(result));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("client failed to receive data");
            }
        }).start();
    }
}
