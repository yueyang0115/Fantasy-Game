package edu.duke.ece.fantasy.database;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Shop")
@PrimaryKeyJoinColumn(name = "ID")
public class Shop extends Building implements Trader{
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ItemPack> inventory = new ArrayList<>();

    public Shop() {
        super("shop");
    }

    public Shop(String name) {
        super(name);
    }

    public Shop addInventory(ItemPack item) {
        inventory.add(item);
        item.setShop(this);
        return this;
    }

    public List<ItemPack> getInventory() {
        return inventory;
    }

    public void setInventory(List<ItemPack> inventory) {
        this.inventory = inventory;
    }

    @Override
    public boolean checkMoney(int required_money) {
        return true;
    }

    @Override
    public boolean checkItem(ItemPack itemPack, int amount) {
        for (ItemPack item : inventory) {
            if (item.getItem().getId() == itemPack.getItem().getId()) {
                return item.getAmount() >= amount;
            }
        }
        return false;
    }

    @Override
    public void sellItem(ItemPack itemPack, int amount) {
        int left_amount = itemPack.getAmount() - amount;
        itemPack.setAmount(left_amount);
        if (left_amount == 0) {
            this.getInventory().remove(itemPack);
            itemPack.setShop(null);
        }
    }

    @Override
    public void buyItem(ItemPack select_item, int amount) {
        boolean find = false;
        for (ItemPack item : inventory) {
            if (item.getItem().getId() == select_item.getItem().getId()) { // if have this type of item
                int init_amount = item.getAmount();
                item.setAmount(init_amount + amount);
                find = true;
            }
        }
        if (!find) {
            ItemPack new_item = new ItemPack(select_item.getItem(), amount);
            addInventory(new_item);
        }
    }
}
