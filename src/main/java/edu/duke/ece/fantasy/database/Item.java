package edu.duke.ece.fantasy.database;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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

    @ManyToMany(mappedBy = "inventory")
    private List<Shop> shop_list;

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

    public List<Shop> getShop_list() {
        return shop_list;
    }

    public void setShop_list(List<Shop> shop_list) {
        this.shop_list = shop_list;
    }

    public void addShop(Shop shop){
        shop_list.add(shop);
    }

    public Item(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }
}
