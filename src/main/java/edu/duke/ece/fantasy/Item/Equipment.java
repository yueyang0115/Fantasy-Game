package edu.duke.ece.fantasy.Item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece.fantasy.ObjectMapperFactory;
import edu.duke.ece.fantasy.database.*;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.IOException;

public class Equipment extends Item {
    int hp;
    int atk;
    int speed;

    public Equipment() {
    }

    public Equipment(String name, int cost, int hp, int atk, int speed) {
        super(name, cost);
        this.hp = hp;
        this.atk = atk;
        this.speed = speed;
    }

    protected String getRequirementWarning(Unit unit) {
        return null;
    }

    public void OnEquip(Unit unit, Player player) {
        if (unit.getWeapon() != null) {
            unit.getWeapon().toGameItem().OnDeEquip(unit);
            player.addItem(new playerInventory(unit.getWeapon(), 1, player));
        }
        unit.addAtk(atk);
        unit.addSpeed(speed);
        unit.setWeapon(this.toDBItem());
    }

    public void OnDeEquip(Unit unit) {
        unit.reduceAtk(atk);
        unit.reduceSpeed(speed);
    }

    @JsonIgnore
    public String getChangeableProperties() {
        // get current class and its parent class's changeable field
        // get parent class's changeable field
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
            ObjectNode objectNode = objectMapper.getNodeFactory().objectNode();
            // add current class's changeable field
//            objectNode.put("usage_limit", usage_limit);
            return objectMapper.writeValueAsString(objectNode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public DBItem toDBItem() {
        return new DBItem(this.getClass().getName(), getChangeableProperties());
    }

    @Override
    public void OnUse(Unit unit, Player player) {
        OnEquip(unit,player);
    }

}
