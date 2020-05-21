package edu.duke.ece.fantacy;

import org.json.JSONObject;

public class PlayerHandler extends Thread{
    private int wid;
    private TCPCommunicator TCPcommunicator;
    private UDPCommunicator UDPcommunicator;
    private DBprocessor myDBprocessor;

    private JsonToAttribute jsonToAttribute;

    public PlayerHandler(TCPCommunicator TCPcm, UDPCommunicator UDPcm, int ID){
        this.wid = ID;
        this.TCPcommunicator = TCPcm;
        this.UDPcommunicator = UDPcm;
    }

    public PlayerHandler(TCPCommunicator TCPcm, UDPCommunicator UDPcm, DPprocessor processor, int ID) {
        this.wid = ID;
        this.TCPcommunicator = TCPcm;
        this.UDPcommunicator = UDPcm;
        this.myDBprocessor = processor;
    }

    public void run() {
        startPlay();
    }

    public void startPlay(){

        //first handle sign-up or log-in, wait until log-in succeed
        boolean loginStatus = false;
        while(!loginStatus){
            String login_msg = TCPcommunicator.receive();
            LoginHandler myLoginHandler = new LoginHandler(login_msg, myDBprocessor, wid);

            String result = myLoginHandler.getLoginResult();
            TCPcommunicator.sendString(result);
            loginStatus = myLoginHandler.getLoginStatus();
        }

        // create a thread for constantly receiving position
        new Thread(()->{
            while(true){
                String position_str = TCPcommunicator.receive();
                JSONObject position_obj = new JSONObject(position_str);
                Position position = (new Deserializer().readPosition(position_obj));
                //get virtual map
                //TODO: call TerritoryHandler, return ?
                //Territory territory = (new TerritoryHandler(position)).getResult();
            }
        }).start();


    }

    public Attribute transformAttribute(Attribute attribute){
        return attribute;
    }

}
