package edu.duke.ece.fantasy.database;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "UnitEquipment")
public class UnitEquipment extends Inventory {
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    public UnitEquipment() {
    }

    public UnitEquipment(DBItem item, int amount, Unit unit) {
        super(item, amount);
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
