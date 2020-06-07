package edu.duke.ece.fantasy.database;

public interface Trader {
    public boolean checkMoney(int required_money);

    public boolean checkItem(ItemPack itemPack, int amount);

    public void removeItem(ItemPack itemPack, int amount);

    public void addItem(ItemPack itemPack, int amount);
}
