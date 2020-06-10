package edu.duke.ece.fantasy.database;

import org.hibernate.Session;
import org.hibernate.query.Query;

public class EquipmentDAO extends ItemDAO {


    public EquipmentDAO(Session session) {
        super(session);
    }


    public void addEquipment(String name, int cost, int hp, int atk, int speed) {
        Item item = getItem(name);
        if (item == null) {
            item = new Equipment(name, cost, hp, atk, speed);
            session.save(item);
        }
    }

    public void initial() {
        addEquipment("sword", 100, 0, 20, 0);
    }
}
