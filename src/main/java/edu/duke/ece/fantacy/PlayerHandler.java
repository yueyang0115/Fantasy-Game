package edu.duke.ece.fantacy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantacy.json.MessageHelper;
import edu.duke.ece.fantacy.json.MessagesC2S;
import edu.duke.ece.fantacy.json.MessagesS2C;

public class PlayerHandler extends Thread{
    private int wid;
    private TCPCommunicator TCPcommunicator;
    private UDPCommunicator UDPcommunicator;
    private DBprocessor myDBprocessor;
    private ObjectMapper myObjectMapper;

    public PlayerHandler(TCPCommunicator TCPcm, UDPCommunicator UDPcm){
        this.TCPcommunicator = TCPcm;
        this.UDPcommunicator = UDPcm;
    }

    public PlayerHandler(TCPCommunicator TCPcm, UDPCommunicator UDPcm, DBprocessor processor) {
        this.TCPcommunicator = TCPcm;
        this.UDPcommunicator = UDPcm;
        this.myDBprocessor = processor;
        this.myObjectMapper = new ObjectMapper();
    }

    public void run() {
        startPlay();
    }

    public void startPlay(){
        while(true){
            MessagesC2S request = TCPcommunicator.receive();
            String request_str = "";
            try {
                request_str = myObjectMapper.writeValueAsString(request);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            System.out.println("TCPcoummunicator receive:" + request_str);

            MessageHandler messageHandler = new MessageHandler(myDBprocessor, wid);
            MessagesS2C result = messageHandler.handle(request);
            wid = messageHandler.getWid();

            TCPcommunicator.send(result);
            String result_str = "";
            try {
                result_str = myObjectMapper.writeValueAsString(result);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            System.out.println("TCPcoummunicator send " +result_str);
        }
    }

}
