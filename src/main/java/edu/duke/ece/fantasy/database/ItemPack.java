package edu.duke.ece.fantasy.database;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "ItemPack")
public class ItemPack {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @JsonBackReference(value = "player-items")
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "amount", nullable = false)
    private int amount;

    public ItemPack(Item item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    public ItemPack() {
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Shop getShop() {
        return shop;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int number) {
        this.amount = number;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass().equals(ItemPack.class)) {
            ItemPack itemPack = (ItemPack) o;
            return itemPack.getItem().getId() == ((ItemPack) o).getItem().getId();
        }
        return false;
    }
}
