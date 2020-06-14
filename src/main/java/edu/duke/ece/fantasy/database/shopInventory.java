package edu.duke.ece.fantasy.database;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "shopInventory")
public class shopInventory extends Inventory {

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    public shopInventory() {
    }

    public shopInventory(DBItem item, int amount, Shop shop) {
        super(item, amount);
        this.shop = shop;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @Override
    public int getOwnerID() {
        return (shop != null) ? shop.getId() : -1;
    }
}
