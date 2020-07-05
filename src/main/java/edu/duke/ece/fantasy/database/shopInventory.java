package edu.duke.ece.fantasy.database;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "shopInventory")
public class shopInventory extends Inventory {
    @Embedded
    private WorldCoord coord;
//    private DBBuilding Shop;

    public shopInventory() {
    }

    public shopInventory(DBItem item, int amount) {
        super(item, amount);
    }

    public shopInventory(DBItem item, int amount, WorldCoord coord) {
        super(item, amount);
        this.coord = coord;
    }

    public WorldCoord getCoord() {
        return coord;
    }

    public void setCoord(WorldCoord coord) {
        this.coord = coord;
    }
}
