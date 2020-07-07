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
//    private String type = "monster";

//    @Column(name = "monster_type", unique = false, nullable = false, length = 100)
//    private String m_type;

    @Embedded
    private WorldCoord coord;

    @Column(name = "need_update", unique = false, nullable = false)
    private boolean needUpdate = true;


  /*@JsonBackReference
    @ManyToOne
    @JoinColumn(name="territory_id", nullable=false)
    private Territory territory;*/

    public Monster(){
        this.setType("monster");
    }

    public Monster(String name,int hp,int atk,int speed){
        this.setType("monster");
        this.setName(name);
        this.setHp(hp);
        this.setAtk(atk);
        this.setSpeed(speed);
    }

    public Monster(Monster old_monster){
        this.setType("monster");
        this.setName(old_monster.getType());
        this.setHp(old_monster.getHp());
        this.setAtk(old_monster.getAtk());
        this.setSpeed(old_monster.getSpeed());
    }

    public WorldCoord getCoord() { return coord; }

    public void setCoord(WorldCoord coord) { this.coord = coord; }

    public boolean isNeedUpdate() { return needUpdate; }

    public void setNeedUpdate(boolean needUpdate) { this.needUpdate = needUpdate; }

    @Override
    public boolean equals(Object o) {
        if (o.getClass().equals(Monster.class)) {
            Monster m = (Monster) o;
            return this.coord==m.coord && this.getId()==m.getId();
        }
        return false;
    }
    @Override
    public int hashCode(){
        return this.toString().hashCode();
    }
    @Override
    public String toString(){
        return (this.getId() + ":" + this.coord.getX() + "," + this.coord.getY());
    }

}
