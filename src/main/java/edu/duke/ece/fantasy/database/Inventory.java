package edu.duke.ece.fantasy.database;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Inventory")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Inventory {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @JoinColumn(name = "item_name", nullable = false)
    private String item_name;

    @Column(name = "amount", nullable = false)
    private int amount;

    public Inventory() {
    }

    public Inventory(String item_name, int amount) {
        this.item_name = item_name;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public abstract int getOwnerID();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inventory)) return false;
        Inventory inventory = (Inventory) o;
        return Objects.equals(item_name, inventory.item_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item_name);
    }
}
