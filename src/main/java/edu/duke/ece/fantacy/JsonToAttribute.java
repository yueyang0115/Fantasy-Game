package edu.duke.ece.fantacy;

import org.json.*;

public class JsonToAttribute {
    private String attributeStr;
    private Attribute myattribute;
    private Location mylocation;

    public JsonToAttribute(String input){
        this.attributeStr = input;
        this.myattribute = new Attribute();
        this.mylocation = new Location();
        JSONObject attributeObj = new JSONObject(attributeStr);
        this.myattribute = JsonToattribute(attributeObj);
    }

    public Attribute JsonToattribute(JSONObject attributeObj){
        Attribute attribute = new Attribute();
        JSONObject locationObj = new JSONObject();
        locationObj = attributeObj.optJSONObject("location");
        if(locationObj != null){
            this.mylocation = JsonToLocation(locationObj);
            attribute.setLocation(this.mylocation);
        }
        return attribute;
    }

    public Location JsonToLocation(JSONObject locationObj){
        Location location = new Location();
        int x = locationObj.optInt("x");
        int y = locationObj.optInt("y");
        location.setX(x);
        location.setY(y);
        return location;
    }

    public Attribute getAttribute(){
        return this.myattribute;
    }

    public Location getLocation(){
        return this.mylocation;
    }
}
