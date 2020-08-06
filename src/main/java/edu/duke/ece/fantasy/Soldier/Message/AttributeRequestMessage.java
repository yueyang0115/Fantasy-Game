package edu.duke.ece.fantasy.Soldier.Message;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.duke.ece.fantasy.World.Message.CmdWorld;
import edu.duke.ece.fantasy.net.Message;
import edu.duke.ece.fantasy.net.MessageMeta;
import edu.duke.ece.fantasy.net.Modules;

@JsonInclude(JsonInclude.Include.NON_NULL)
@MessageMeta(module = Modules.SOLDIER, cmd = CmdSoldier.REQ_ATTRIBUTE)
public class AttributeRequestMessage extends Message {
    private String type;

    public AttributeRequestMessage() {
    }

    public AttributeRequestMessage(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
