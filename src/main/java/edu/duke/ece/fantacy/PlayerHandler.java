package edu.duke.ece.fantacy;

import edu.duke.ece.fantacy.json.MessageHelper;
import edu.duke.ece.fantacy.json.MessagesC2S;
import edu.duke.ece.fantacy.json.MessagesS2C;
import org.json.*;
import java.util.*;

public class PlayerHandler extends Thread{
    private int wid;
    private TCPCommunicator TCPcommunicator;
    private UDPCommunicator UDPcommunicator;
    private DBprocessor myDBprocessor;

    public PlayerHandler(TCPCommunicator TCPcm, UDPCommunicator UDPcm){
        this.TCPcommunicator = TCPcm;
        this.UDPcommunicator = UDPcm;
    }

    public PlayerHandler(TCPCommunicator TCPcm, UDPCommunicator UDPcm, DBprocessor processor) {
        this.TCPcommunicator = TCPcm;
        this.UDPcommunicator = UDPcm;
        this.myDBprocessor = processor;
    }

    public void run() {
        startPlay();
    }

    public void startPlay(){
        while(true){
            String recvMsg= TCPcommunicator.receive();
            System.out.println("TCPcoummunicator receive:" + recvMsg);
            MessagesC2S request = (new MessageHelper().deserialize(recvMsg));

            MessageHandler messageHandler = new MessageHandler(myDBprocessor, wid);
            MessagesS2C result = messageHandler.handle(request);
            wid = messageHandler.getWid();

            String sendMsg = (new MessageHelper().serialize(result));
            TCPcommunicator.sendString(sendMsg);
            System.out.println("TCPcoummunicator send " + sendMsg);
        }
    }

}
