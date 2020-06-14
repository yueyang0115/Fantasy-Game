package edu.duke.ece.fantasy.Item;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.database.DBItem;

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

    public DBItem toDBItem() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return new DBItem(this.getClass().getName(), objectMapper.writeValueAsString(this));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


//    public abstract void useItem(Unit unit);

}
