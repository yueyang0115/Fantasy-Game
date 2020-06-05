package edu.duke.ece.fantasy.database;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Shop")
@PrimaryKeyJoinColumn(name = "ID")
public class Shop extends Building {
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "Shop_Item",
            joinColumns = { @JoinColumn(name = "shop_id") },
            inverseJoinColumns = { @JoinColumn(name = "item_id") }
    )
    @JsonManagedReference
    private List<Item> inventory = new ArrayList<>();

    public Shop() {
    }

    public Shop(String name) {
        super(name);
    }

    public Shop addInventory(Item item) {
        inventory.add(item);
        item.addShop(this);
        return this;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }
}
