package edu.duke.ece.fantasy.database;

import java.lang.reflect.InvocationTargetException;

public interface Trader {
    public boolean checkMoney(int required_money);

    public boolean checkItem(Inventory inventory, int amount);

    public void sellItem(Inventory inventory, int amount) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;

    public void buyItem(Inventory inventory, int amount) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;
}
