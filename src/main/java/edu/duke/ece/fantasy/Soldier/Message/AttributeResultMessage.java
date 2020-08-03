package edu.duke.ece.fantasy.Soldier.Message;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.net.Message;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttributeResultMessage extends Message {
    private List<Soldier> soldiers;

    public AttributeResultMessage() {
    }

    public AttributeResultMessage(List<Soldier> soldiers) {
        this.soldiers = soldiers;
    }

    public List<Soldier> getSoldiers() {
        return soldiers;
    }

    public void setSoldiers(List<Soldier> soldiers) {
        this.soldiers = soldiers;
    }
}
