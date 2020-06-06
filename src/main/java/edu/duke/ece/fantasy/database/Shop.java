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
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ItemPack> inventory = new ArrayList<>();

    public Shop() {
        super("shop");
    }

    public Shop(String name) {
        super(name);
    }

    public Shop addInventory(ItemPack item) {
        inventory.add(item);
        item.setShop(this);
        return this;
    }

    public List<ItemPack> getInventory() {
        return inventory;
    }

    public void setInventory(List<ItemPack> inventory) {
        this.inventory = inventory;
    }
}
