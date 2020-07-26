package edu.duke.ece.fantasy.database;

import edu.duke.ece.fantasy.Item.InvalidItemUsageException;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Inventory")
@Inheritance(strategy = InheritanceType.JOINED)
public class Inventory {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Embedded
    private DBItem item;

    @Column(name = "amount", nullable = false)
    private int amount;

    public Inventory() {
    }

    public Inventory(DBItem item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    public Inventory(int id, DBItem item, int amount) {
        this.id = id;
        this.item = item;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DBItem getDBItem() {
        return item;
    }

    public void setDBItem(DBItem item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void reduceAmount(int reduceAmount) {
        amount -= reduceAmount;
    }

    public Inventory toClient() {
        return new Inventory(id, item.toGameItem().toClient(), amount);
    }

    public void useItem(Unit unit,Player player) {
        this.getDBItem().toGameItem().OnUse(unit,player);
        this.reduceAmount(1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inventory)) return false;
        Inventory inventory = (Inventory) o;
        return Objects.equals(item, inventory.item) || Objects.equals(id, inventory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item);
    }
}
