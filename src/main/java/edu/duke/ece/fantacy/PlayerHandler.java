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

        while(true){
            //receive first attribute, including real address
            //String attributeStr = TCPcommunicator.receive();
            String attributeStr = UDPcommunicator.receive();
            System.out.println("[DEBUG] server receive attribute: " +attributeStr);

            JsonToAttribute jsonToattribute = new JsonToAttribute(attributeStr);
            Attribute attribute = jsonToattribute.getAttribute();

            //send attribute, including virtual address and others
            Attribute v_attribute = transformAttribute(attribute);
            JSONObject v_attributeObj = new JSONObject();
            AttributeToJson attributeToJson = new AttributeToJson(v_attribute);
            v_attributeObj = attributeToJson.getAttributeObj();

            //TCPcommunicator.sendString(v_attributeObj.toString());
            UDPcommunicator.SendString(v_attributeObj.toString());
            System.out.println("[DEBUG] server send virtual attribute: " +  v_attributeObj.toString()+"\n");
        }

    }

    public Attribute transformAttribute(Attribute attribute){
        return attribute;
    }

}
