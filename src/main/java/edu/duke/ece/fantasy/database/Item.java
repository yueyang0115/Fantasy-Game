package edu.duke.ece.fantasy.database;


import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Item")
public class Item {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "ID", unique = true, nullable = false)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "cost")
    private int cost;

//    @JsonBackReference
//    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
//    private List<ItemPack> itemPack;

    public Item() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

//    public List<ItemPack> getItemPack() {
//        return itemPack;
//    }
//
//    public void setItemPack(List<ItemPack> itemPack) {
//        this.itemPack = itemPack;
//    }

    public Item(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }
}
