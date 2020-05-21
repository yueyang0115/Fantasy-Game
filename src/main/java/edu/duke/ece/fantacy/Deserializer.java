package edu.duke.ece.fantacy;

import org.json.*;

public class Deserializer {
    //from JSONObject to Position object
    public Position readPosition(JSONObject js){
        Position position = new Position();
        position.setX(js.optDouble("x"));
        position.setX(js.optDouble("y"));
        return position;
    }
}
