package edu.duke.ece.fantasy.database;

public interface Trader {
    public boolean checkMoney(int required_money);

    public boolean checkItem(ItemPack itemPack, int amount);

    public void sellItem(ItemPack itemPack, int amount);

    public void buyItem(ItemPack itemPack, int amount);
}
