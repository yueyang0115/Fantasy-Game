package edu.duke.ece.fantasy.Item;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.duke.ece.fantasy.ObjectMapperFactory;
import edu.duke.ece.fantasy.database.DBItem;
import edu.duke.ece.fantasy.database.Unit;
import org.json.JSONObject;

public abstract class Item implements IItem {
    private String name;

    private int cost;


    public Item() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Item(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

//    public String getChangeableProperties(){
//        return "";
//    }

    public DBItem toDBItem() {
        return new DBItem(this.getClass().getName(), "");
    }

    public DBItem toClient(){
        try{
            return new DBItem("", ObjectMapperFactory.getObjectMapper().writeValueAsString(this));
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public abstract void OnUse(Unit unit);

}
