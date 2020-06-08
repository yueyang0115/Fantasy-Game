package edu.duke.ece.fantasy.database;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;
import org.jasypt.hibernate5.type.EncryptedStringType;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;


@Entity
@Table(name = "Player")
public class Player implements Trader {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "ID", unique = true, nullable = false)
    private int id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "money")
    private int money;

    @Column(name = "WID", columnDefinition = "serial", unique = true, insertable = false, updatable = false, nullable = false)
    private int wid;

    @JsonManagedReference
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<Soldier> soldiers = new ArrayList<>();

    @JsonManagedReference(value = "player-items")
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<ItemPack> items = new ArrayList<>();

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Player() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public List<Soldier> getSoldiers() {
        return soldiers;
    }

    public void setSoldiers(List<Soldier> soldiers) {
        this.soldiers = soldiers;
    }

    public void addSoldier(Soldier soldier) {
        soldier.setPlayer(this);
        this.soldiers.add(soldier);
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public List<ItemPack> getItems() {
        return items;
    }

    public void setItems(List<ItemPack> items) {
        this.items = items;
    }

    public void addItem(ItemPack item) {
        items.add(item);
        item.setPlayer(this);
    }

    @Override
    public boolean checkMoney(int required_money) {
        return money >= required_money;
    }

    @Override
    public boolean checkItem(ItemPack itemPack, int amount) {
        for (ItemPack item : items) {
            if (item.getItem().getId() == itemPack.getItem().getId()) { // if have this type of item
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
            this.getItems().remove(itemPack);
            itemPack.setPlayer(null);
        }
        money += amount * itemPack.getItem().getCost();
    }

    @Override
    public void buyItem(ItemPack select_item, int amount) {
        boolean find = false;
        for (ItemPack item : items) {
            if (item.getItem().getId() == select_item.getItem().getId()) { // if have this type of item
                int init_amount = item.getAmount();
                item.setAmount(init_amount + amount);
                find = true;
            }
        }
        money -= amount * select_item.getItem().getCost();
        if (!find) {
            ItemPack new_item = new ItemPack(select_item.getItem(), amount);
            addItem(new_item);
        }
    }
}
