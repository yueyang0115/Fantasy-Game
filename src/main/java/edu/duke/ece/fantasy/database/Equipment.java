package edu.duke.ece.fantasy.database;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Equipment")
public class Equipment extends Item {

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @Override
    public void useItem(Unit unit) {

    }
}
