package edu.duke.ece.fantasy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.json.MessageHelper;
import edu.duke.ece.fantasy.json.MessagesC2S;
import edu.duke.ece.fantasy.json.MessagesS2C;

import java.io.IOException;

public class PlayerHandler extends Thread{
    private int wid;
    private TCPCommunicator TCPcommunicator;
    private UDPCommunicator UDPcommunicator;
    private DBprocessor myDBprocessor;
    private ObjectMapper myObjectMapper;

    public PlayerHandler(TCPCommunicator TCPcm, UDPCommunicator UDPcm){
        this.TCPcommunicator = TCPcm;
        this.UDPcommunicator = UDPcm;
        this.myObjectMapper = new ObjectMapper();
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
        while(!TCPcommunicator.isClosed()){
            try{
                MessagesC2S request = TCPcommunicator.receive();
                if(TCPcommunicator.isClosed()) break;
                String request_str = "";
                request_str = myObjectMapper.writeValueAsString(request);
                System.out.println("[DEBUG] TCPcommunicator successfully receive:" + request_str);

                MessageHandler messageHandler = new MessageHandler(myDBprocessor, wid);
                MessagesS2C result = messageHandler.handle(request);
                wid = messageHandler.getWid();

                TCPcommunicator.send(result);
                if(TCPcommunicator.isClosed()) break;
                String result_str = "";
                result_str = myObjectMapper.writeValueAsString(result);
                System.out.println("[DEBUG] TCPcommunicator successfully send " +result_str);
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
