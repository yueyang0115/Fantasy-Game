package edu.duke.ece.fantasy.Soldier.Message;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.duke.ece.fantasy.Account.CmdAccount;
import edu.duke.ece.fantasy.World.Message.CmdWorld;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.net.Message;
import edu.duke.ece.fantasy.net.MessageMeta;
import edu.duke.ece.fantasy.net.Modules;

import java.util.*;

@MessageMeta(module = Modules.SOLDIER, cmd = CmdSoldier.RES_ATTRIBUTE)
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
