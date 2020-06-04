package edu.duke.ece.fantasy.database;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Table( name = "Monster" )
public class Monster extends Unit{

    //@Column(name = "unit_type", unique = false, nullable = false, length = 100)
    private String u_type = "monster";

    @Column(name = "monster_type", unique = false, nullable = false, length = 100)
    private String m_type;

    @Column(name = "HP", unique = false, nullable = false)
    private int hp;

    @Column(name = "ATK", unique = false, nullable = false)
    private int atk;

//    @Column(name = "speed", unique = false, nullable = false)
//    private int speed = this.getSpeed();

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="territory_id", nullable=false)
    private Territory territory;

    public Monster(){

    }

    public Monster(String type,int hp,int atk,int speed){
        this.m_type = type;
        this.hp = hp;
        this.atk = atk;
        this.setSpeed(speed);
    }

    public Monster(Monster old_monster){
        this.m_type = old_monster.getType();
        this.hp = old_monster.getHp();
        this.atk = old_monster.getAtk();
        this.setSpeed(old_monster.getSpeed());
    }

    public String getType() {
        return m_type;
    }

    public void setType(String type) {
        this.m_type = type;
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

    public Territory getTerritory() {
        return territory;
    }

    public void setTerritory(Territory territory) {
        this.territory = territory;
    }

    public JSONObject toJSON(){
        JSONObject monster_obj = new JSONObject();
//        monster_obj.put("id",id);
        monster_obj.put("type",m_type);
        monster_obj.put("hp",hp);
        monster_obj.put("atk",atk);
        return monster_obj;
    }
}
