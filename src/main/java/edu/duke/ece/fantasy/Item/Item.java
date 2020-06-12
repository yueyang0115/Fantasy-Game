package edu.duke.ece.fantasy.Item;


import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.duke.ece.fantasy.database.Unit;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

//    public abstract void useItem(Unit unit);

}
