package edu.duke.ece.fantasy.database;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.duke.ece.fantasy.Item.InvalidItemUsageException;
import edu.duke.ece.fantasy.Item.Item;
import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "Player")
public class Player implements Trader {
    public enum Status {
        INBUILDING, INBATTLE, INMAIN, INBAG, INLEVELUP;
    }

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

    @Column(name = "moneyGenerationSpeed")
    private int moneyGenerationSpeed = 0;

    @Column(name = "WID", columnDefinition = "serial", unique = true, insertable = false, updatable = false, nullable = false)
    @Generated(GenerationTime.INSERT)
    private int wid;

    @Column(name = "status", nullable = false)
    private Status status = Status.INMAIN;

    @Column(name = "coordX")
    private int coordX;

    @Column(name = "coordY")
    private int coordY;

    @JsonManagedReference
    @OneToMany(mappedBy = "player", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Soldier> soldiers = new ArrayList<>();

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<playerInventory> items = new ArrayList<>();

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Player() {
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public WorldCoord getCurrentCoord() {
        return new WorldCoord(wid, coordX, coordY);
    }

    public void setCurrentCoord(WorldCoord currentCoord) {
        this.coordX = currentCoord.getX();
        this.coordY = currentCoord.getY();
    }

    public int getMoneyGenerationSpeed() {
        return moneyGenerationSpeed;
    }

    public void setMoneyGenerationSpeed(int moneyGenerationSpeed) {
        this.moneyGenerationSpeed = moneyGenerationSpeed;
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

    public List<playerInventory> getItems() {
        return items;
    }

    public void setItems(List<playerInventory> items) {
        this.items = items;
    }

    public void addItem(playerInventory item) {
        playerInventory pairedInventory = findInventoryFromList(item);
        if (pairedInventory != null) {
            pairedInventory.setAmount(pairedInventory.getAmount() + item.getAmount());
        } else {
            System.out.println("Didn't find the same Inventory");
            items.add(item);
            item.setPlayer(this);
        }
    }

    private void reduceItem(Inventory inventory, int amount) {
        playerInventory playerInventory = (playerInventory) inventory;
        int left_amount = inventory.getAmount() - amount;
        inventory.setAmount(left_amount);
        if (left_amount == 0) {
            this.getItems().remove(inventory);
            playerInventory.setPlayer(null);
        }
    }

//    public void useItem(Inventory inventory, int amount, Unit unit) throws InvalidItemUsageException {
//        inventory.getDBItem().toGameItem().OnUse(unit);
//        reduceItem(inventory, amount);
//    }
//
//    public void dropItem(Inventory inventory, int amount) {
//        reduceItem(inventory, amount);
//    }


    private playerInventory findInventoryFromList(Inventory selectedInventory) {
        playerInventory pairedInventory = null;
        System.out.println("I'm in find");
        for (playerInventory inventory : items) {
            System.out.println("check item-"+inventory.getDBItem().toString());
            if (inventory.equals(selectedInventory)) { // check if inventoryList have the same item as selectedInventory
                pairedInventory = inventory;
                break;
            }
        }
        return pairedInventory;
    }

    @Override
    public boolean checkMoney(int required_money) {
        return money >= required_money;
    }

    @Override
    public boolean checkItem(Inventory inventory, int amount) {
        for (Inventory item : items) {
            if (item.equals(inventory)) { // if have this type of item
                return item.getAmount() >= amount;
            }
        }
        return false;
    }

    @Override
    public void addMoney(int money) {
        this.money += money;
    }

    @Override
    public void subtractMoney(int money) {
        this.money -= money;
    }

    @Override
    public Inventory addInventory(MetaDAO metaDAO, Inventory inventory) {
        playerInventory playerInventory = new playerInventory(inventory.getDBItem().toGameItem().toDBItem(), inventory.getAmount(), this);
        metaDAO.getSession().save(playerInventory);
        this.addItem(playerInventory);
        return playerInventory;
    }

}
