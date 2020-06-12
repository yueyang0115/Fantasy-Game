package edu.duke.ece.fantasy.Item;

import edu.duke.ece.fantasy.database.Unit;

public interface IItem {
    int getCost();
    void OnUse(Unit unit);
}
