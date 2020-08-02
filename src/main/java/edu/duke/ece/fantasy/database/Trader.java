package edu.duke.ece.fantasy.database;

import edu.duke.ece.fantasy.database.DAO.MetaDAO;

import java.lang.reflect.InvocationTargetException;

public interface Trader {
//    public int getId();

    public boolean checkMoney(int required_money);

    public boolean checkItem(Inventory inventory, int amount);
    public void addMoney(int money);
    public void subtractMoney(int money);
    public Inventory addInventory(Inventory inventory);
//
//    public void sellItem(Inventory inventory, int amount);
//
//    public Inventory buyItem(Inventory inventory, int amount);
}
