package edu.duke.ece.fantasy.Item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece.fantasy.ObjectMapperFactory;
import edu.duke.ece.fantasy.database.DBItem;
import edu.duke.ece.fantasy.database.Unit;
import edu.duke.ece.fantasy.database.UnitEquipment;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.IOException;

public class Equipment extends Item {
    int hp;
    int atk;
    int speed;
    int usage_limit;

    public Equipment() {
    }

    public Equipment(String name, int cost, int hp, int atk, int speed, int usage_limit) {
        super(name, cost);
        this.hp = hp;
        this.atk = atk;
        this.speed = speed;
        this.usage_limit = usage_limit;
    }

    private boolean meetRequirement(Unit unit) {
        return true;
    }

    public void OnEquip(Unit unit) {
        if (meetRequirement(unit)) {
            unit.addEquipment(new UnitEquipment(this.toDBItem(),1,unit));
        }
    }

    public int getUsage_limit() {
        return usage_limit;
    }

    public void setUsage_limit(int usage_limit) {
        this.usage_limit = usage_limit;
    }

    public void OnDeEquip() {

    }

    @JsonIgnore
    public String getChangeableProperties() {
        // get current class and its parent class's changeable field
        // get parent class's changeable field
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
            ObjectNode objectNode = objectMapper.getNodeFactory().objectNode();
            // add current class's changeable field
            objectNode.put("usage_limit", usage_limit);
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
    public void OnUse(Unit unit) {
        OnEquip(unit);
    }

}
