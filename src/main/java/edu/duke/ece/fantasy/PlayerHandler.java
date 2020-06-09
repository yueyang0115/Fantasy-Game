package edu.duke.ece.fantasy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.json.MessageHelper;
import edu.duke.ece.fantasy.json.MessagesC2S;
import edu.duke.ece.fantasy.json.MessagesS2C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class PlayerHandler extends Thread{
    private int wid;
    private TCPCommunicator TCPcommunicator;
    private UDPCommunicator UDPcommunicator;
    private DBprocessor myDBprocessor;
    private ObjectMapper myObjectMapper;
    MessageHandler messageHandler;
    Logger log = LoggerFactory.getLogger(Player.class);

    public PlayerHandler(TCPCommunicator TCPcm, UDPCommunicator UDPcm){
        this.TCPcommunicator = TCPcm;
        this.UDPcommunicator = UDPcm;
        this.myObjectMapper = new ObjectMapper();
        this.messageHandler = new MessageHandler(TCPcm);
    }

    public void run() {
        startPlay();
    }

    public void startPlay(){
        while(!TCPcommunicator.isClosed()){
            try{
                MessagesC2S request = TCPcommunicator.receive();
                if(TCPcommunicator.isClosed()) break;
                String request_str = "";
                request_str = myObjectMapper.writeValueAsString(request);
                System.out.println("[DEBUG] TCPcommunicator successfully receive:" + request_str);
//                Instant start = Instant.now();

                messageHandler.handle(request);
                wid = messageHandler.getWid();
                if(TCPcommunicator.isClosed()) break;

//                Instant end = Instant.now();
//                Duration timeElapsed  = Duration.between(start,end);
//                log.info("time used in handling message is {} Nanoseconds",timeElapsed.getNano());
            }
            catch(IOException e){
                e.printStackTrace();
                if(TCPcommunicator.isClosed()) {
                    System.out.println("[DEBUG] Client socket might closed, prepare to exit");
                }
            }
        }
        TCPcommunicator.close();
        System.out.println("[DEBUG] Client socket might closed, close corresponding thread in server");
    }

}
