//package edu.duke.ece.fantasy.database;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//import javax.persistence.*;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Table(name = "Shop")
//@PrimaryKeyJoinColumn(name = "ID")
//public class DBShop extends DBBuilding implements Trader {
//
//    @OneToMany(mappedBy = "Shop", cascade = CascadeType.ALL)
//    @JsonIgnore
//    private List<shopInventory> items = new ArrayList<>();
//
//    public DBShop() {
//        super("shop");
//    }
//
//    public DBShop(WorldCoord coord) {
//        super("shop", coord);
//    }
//
//    public DBShop addInventory(shopInventory item) {
//        items.add(item);
//        item.setDBShop(this);
//        return this;
//    }
//
//    public List<shopInventory> getItems() {
//        return items;
//    }
//
//    public void setItems(List<shopInventory> items) {
//        this.items = items;
//    }
//
//
//    @Override
//    public boolean checkMoney(int required_money) {
//        return true;
//    }
//
//    @Override
//    public boolean checkItem(Inventory inventory, int amount) {
//        for (Inventory item : items) {
//            if (item == inventory) { // if have this type of item
//                return item.getAmount() >= amount;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public void sellItem(Inventory inventory, int amount) {
//        int left_amount = inventory.getAmount() - amount;
//        inventory.setAmount(left_amount);
//        if (left_amount == 0) {
//            this.getItems().remove(inventory);
//        }
//    }
//
//    @Override
//    public void buyItem(Inventory select_item, int amount) {
//        boolean find = false;
//        for (Inventory item : items) {
//            if (item.equals(select_item)) { // if have this type of item
//                int init_amount = item.getAmount();
//                item.setAmount(init_amount + amount);
//                find = true;
//            }
//        }
//        if (!find) {
//            shopInventory new_item = new shopInventory(select_item.getDBItem(), amount, this);
//            addInventory(new_item);
//        }
//    }
//}
