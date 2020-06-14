package edu.duke.ece.fantasy.database;

import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.duke.ece.fantasy.json.InventoryRequestMessage;
import org.hibernate.annotations.GenericGenerator;

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

    @Override
    public int getOwnerID() {
        return (player != null) ? player.getId() : -1;
    }
}
