package edu.duke.ece.fantasy.database;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.GenericGenerator;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Table( name = "Soldier" )
public class Soldier extends Unit{

    //@Column(name = "unit_type", unique = false, nullable = false, length = 100)
//    private String type = "soldier";

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="player_id", nullable=false)
    private Player player;

    public Soldier(){
        this.setType("soldier");
    }

    public Soldier(String name,int hp,int atk,int speed){
        this.setType("soldier");
        this.setName(name);
        this.setHp(hp);
        this.setAtk(atk);
        this.setSpeed(speed);
    }

    public Soldier(Soldier old_soldier){
        this.setType("soldier");
        this.setName(old_soldier.getType());
        this.setHp(old_soldier.getHp());
        this.setAtk(old_soldier.getAtk());
        this.setSpeed(old_soldier.getSpeed());
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}

