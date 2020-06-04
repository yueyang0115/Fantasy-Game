package edu.duke.ece.fantasy.database;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.GenericGenerator;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Table( name = "Soldier" )
public class Soldier extends Unit{

    //@Column(name = "unit_type", unique = false, nullable = false, length = 100)
    private String u_type = "soldier";

    @Column(name = "soldier_type", unique = false, nullable = false, length = 100)
    private String s_type;

    @Column(name = "HP", unique = false, nullable = false)
    private int hp;

    @Column(name = "ATK", unique = false, nullable = false)
    private int atk;

//    @Column(name = "speed", unique = false, nullable = false)
//    private int speed = this.getSpeed();

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="player_id", nullable=false)
    private Player player;

    public Soldier(){

    }

    public Soldier(String type,int hp,int atk,int speed){
        this.s_type = type;
        this.hp = hp;
        this.atk = atk;
        this.setSpeed(speed);
    }

    public Soldier(Soldier old_soldier){
        this.s_type = old_soldier.getType();
        this.hp = old_soldier.getHp();
        this.atk = old_soldier.getAtk();
        this.setSpeed(old_soldier.getSpeed());
    }

    public String getType() {
        return s_type;
    }

    public void setType(String type) {
        this.s_type = type;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public JSONObject toJSON(){
        JSONObject soldier_obj = new JSONObject();
        //soldier_obj.put("id",id);
        soldier_obj.put("type",s_type);
        soldier_obj.put("hp",hp);
        soldier_obj.put("atk",atk);
        return soldier_obj;
    }
}

