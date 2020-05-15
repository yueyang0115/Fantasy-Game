package edu.duke.ece.fantacy;
import org.json.*;

public class AttributeToJson {
    private JSONObject myattributeObj;
    private JSONObject mylocationObj;

    public AttributeToJson(Attribute attribute){
        this.myattributeObj = new JSONObject();
        this.mylocationObj = new JSONObject();
        this.myattributeObj = attributeTojson(attribute);
        attributeTojson(attribute);
    }

    public JSONObject attributeTojson(Attribute attribute){
        JSONObject attributeObj = new JSONObject();
        this.mylocationObj = locationTojson(attribute.getLocation());
        attributeObj.put("position", this.mylocationObj);
        return attributeObj;
    }

    public JSONObject locationTojson(Location location) {
        JSONObject locationObj = new JSONObject();
        locationObj.put("x",location.getX());
        locationObj.put("y",location.getY());
        return locationObj;
    }

    public JSONObject getAttributeObj(){ return this.myattributeObj; }
    public JSONObject getLocationObj(){ return this.mylocationObj; }
}
