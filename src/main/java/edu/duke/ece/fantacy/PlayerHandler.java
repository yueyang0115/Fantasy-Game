package edu.duke.ece.fantacy;

import org.json.*;
import java.util.*;

public class PlayerHandler extends Thread{
    private int wid;
    private TCPCommunicator TCPcommunicator;
    private UDPCommunicator UDPcommunicator;
    //private MockDBprocessor myMockDBprocessor;
    private DBprocessor myDBprocessor;

    private JsonToAttribute jsonToAttribute;
    public PlayerHandler(TCPCommunicator TCPcm, UDPCommunicator UDPcm, int ID){
        this.wid = ID;
        this.TCPcommunicator = TCPcm;
        this.UDPcommunicator = UDPcm;
    }

    public PlayerHandler(TCPCommunicator TCPcm, UDPCommunicator UDPcm, DBprocessor processor, int ID) {
        this.wid = ID;
        this.TCPcommunicator = TCPcm;
        this.UDPcommunicator = UDPcm;
        //this.myMockDBprocessor = processor;
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
            System.out.println("TCPcoummunicator receive:" + login_msg);
            LoginHandler myLoginHandler = new LoginHandler(login_msg, myDBprocessor, wid);

            String result = myLoginHandler.getLoginResult();
            TCPcommunicator.sendString(result);
            System.out.println("TCPcoummunicator send " + result);
            loginStatus = myLoginHandler.getLoginStatus();
        }

        // create a thread for constantly receiving position
        new Thread(()->{
            while(true){
                //receive position
                String position_str = TCPcommunicator.receive();
                System.out.println("TCPcoummunicator receive position: " + position_str);

                JSONObject position_obj = new JSONObject(position_str);
                Position position = (new Deserializer().readPosition(position_obj));
//                TerritoryHandler myTerritoryHandler = new TerritoryHandler(myDBprocessor);
//                List<Territory> territoryList = myTerritoryHandler.getTerritories();

                //send territoryList
                JSONArray territoryList_arr = new JSONArray();
//                for (int i = 0; i < territoryList.size(); i++) {
//                    Territory t = territoryList.get(i);
//                    territoryList_arr.put(t.toJSON());
//                }
                TCPcommunicator.sendString(territoryList_arr.toString());
            }
        }).start();


    }

    public Attribute transformAttribute(Attribute attribute){
        return attribute;
    }

}
