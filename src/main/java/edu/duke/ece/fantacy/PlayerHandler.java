package edu.duke.ece.fantacy;

import org.json.JSONObject;

public class PlayerHandler extends Thread{
    private int id;
    private TCPCommunicator TCPcommunicator;
    private UDPCommunicator UDPcommunicator;

    private JsonToAttribute jsonToAttribute;

    public PlayerHandler(TCPCommunicator TCPcm, UDPCommunicator UDPcm, int ID){
        this.id = ID;
        this.TCPcommunicator = TCPcm;
        this.UDPcommunicator = UDPcm;
    }

    public void run() {
        startPlay();
    }

    public void startPlay(){
        //Send id to player
//        communicator.sendString(String.valueOf(id));
//        System.out.println("Send player id " + id);

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

            //TCPcommunicator.sendJSON(v_attributeObj);
            UDPcommunicator.SendString(v_attributeObj.toString());
            System.out.println("[DEBUG] server send virtual attribute: " +  v_attributeObj.toString()+"\n");
        }

    }

    public Attribute transformAttribute(Attribute attribute){
        return attribute;
    }

}
