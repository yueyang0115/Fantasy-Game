package edu.duke.ece.fantasy.database;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Shop")
@PrimaryKeyJoinColumn(name = "ID")
public class Shop extends Building {
    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL)
    private List<Item> inventory = new ArrayList<>();

    public List<Item> getInventory() {
        return inventory;
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }
}
