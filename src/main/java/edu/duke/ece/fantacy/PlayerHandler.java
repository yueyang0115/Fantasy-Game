package edu.duke.ece.fantacy;

import org.json.JSONObject;

public class PlayerHandler extends Thread{
    private int id;
    private Communicator communicator;
    private JsonToAttribute jsonToAttribute;

    public PlayerHandler(Communicator cm, int ID){
        this.id = ID;
        this.communicator = cm;
    }

    public void run() {
        startPlay();
    }

    public void startPlay(){
        //Send id to player
//        communicator.sendString(String.valueOf(id));
//        System.out.println("Send player id " + id);

        //receive first attribute, including real address
        String attributeStr = communicator.receive();
        System.out.println("Receive attribute: " +attributeStr);

        JsonToAttribute jsonToattribute = new JsonToAttribute(attributeStr);
        Attribute attribute = jsonToattribute.getAttribute();

        //send attribute, including virtual address and others
        Attribute v_attribute = transformAttribute(attribute);
        JSONObject v_attributeObj = new JSONObject();
        AttributeToJson attributeToJson = new AttributeToJson(v_attribute);
        v_attributeObj = attributeToJson.getAttributeObj();
        communicator.sendJSON(v_attributeObj);
        System.out.println("Send virtual attribute: " +  v_attributeObj.toString());


//        while(true){
//        }

    }

    public Attribute transformAttribute(Attribute attribute){
        return attribute;
    }

}
