package edu.duke.ece.fantasy.database;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Shop")
@PrimaryKeyJoinColumn(name = "ID")
public class Shop extends Building implements Trader {
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<shopInventory> items = new ArrayList<>();

    public Shop() {
        super("shop");
    }

    public Shop(String name) {
        super(name);
    }

    public Shop addInventory(shopInventory item) {
        items.add(item);
        item.setShop(this);
        return this;
    }

    public List<shopInventory> getItems() {
        return items;
    }

    public void setItems(List<shopInventory> items) {
        this.items = items;
    }

    @Override
    public boolean checkMoney(int required_money) {
        return true;
    }

    @Override
    public boolean checkItem(Inventory inventory, int amount) {
        for (Inventory item : items) {
            if (item.getItem_name().equals(inventory.getItem_name())) { // if have this type of item
                return item.getAmount() >= amount;
            }
        }
        return false;
    }

    @Override
    public void sellItem(Inventory inventory, int amount) {
        shopInventory shopInventory = (shopInventory) inventory;
        int left_amount = inventory.getAmount() - amount;
        inventory.setAmount(left_amount);
        if (left_amount == 0) {
            this.getItems().remove(inventory);
            shopInventory.setShop(null);
        }
    }

    @Override
    public void buyItem(Inventory select_item, int amount) {
        boolean find = false;
        for (Inventory item : items) {
            if (item.getItem_name().equals(select_item.getItem_name())) { // if have this type of item
                int init_amount = item.getAmount();
                item.setAmount(init_amount + amount);
                find = true;
            }
        }
        if (!find) {
            shopInventory new_item = new shopInventory(select_item.getItem_name(), amount,this);
            addInventory(new_item);
        }
    }
}
