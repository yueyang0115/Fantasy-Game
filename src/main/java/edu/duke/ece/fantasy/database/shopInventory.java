package edu.duke.ece.fantasy.database;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "shopInventory")
public class shopInventory extends Inventory {

//    @JsonIgnore
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "coord_wid", referencedColumnName = "wid"),
            @JoinColumn(name = "coord_x", referencedColumnName = "x"),
            @JoinColumn(name = "coord_y", referencedColumnName = "y")
    })
    private DBBuilding Shop;

    public shopInventory() {
    }

    public shopInventory(DBItem item, int amount) {
        super(item, amount);
    }

    public shopInventory(DBItem item, int amount, DBBuilding DBShop) {
        super(item, amount);
        this.Shop = DBShop;
    }

    public DBBuilding getDBBuilding() {
        return Shop;
    }

    public void setDBBuilding(DBBuilding DBShop) {
        this.Shop = DBShop;
    }


}
