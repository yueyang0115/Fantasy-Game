package edu.duke.ece.fantacy;

import edu.duke.ece.fantacy.json.MessageHelper;
import edu.duke.ece.fantacy.json.MessagesC2S;
import edu.duke.ece.fantacy.json.MessagesS2C;

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
            MessagesC2S request = TCPcommunicator.receive();
            System.out.println("TCPcoummunicator receive:" + request);

            MessageHandler messageHandler = new MessageHandler(myDBprocessor, wid);
            MessagesS2C result = messageHandler.handle(request);
            wid = messageHandler.getWid();

            TCPcommunicator.send(result);
            System.out.println("TCPcoummunicator send " + result.toString());
        }
    }

}
