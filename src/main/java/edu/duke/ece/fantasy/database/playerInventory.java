package edu.duke.ece.fantasy.database;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "playerInventory")
public class playerInventory extends Inventory {
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    public playerInventory(DBItem item, int amount, Player player) {
        super(item, amount);
        this.player = player;
    }

    public playerInventory() {
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
