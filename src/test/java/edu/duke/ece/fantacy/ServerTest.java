package edu.duke.ece.fantacy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServerTest {
//    @Test
//    void buildServerTest() {
//        Server server = new Server();
//        server.main(null);
//    }

    //@Test
    void buildClientTest() {
        for(int i=0;i<10;i++){
            Communicator cm = new Communicator("0.0.0.0", 1234);
            System.out.println("Received id is " + cm.receive());
            cm.sendString("{'position':{'x':'100.00','y':'100.00'}}");
            //System.out.println("Received virtual attribute is " + cm.receive());
        }
    }
}
