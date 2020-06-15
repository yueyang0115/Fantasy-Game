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

    @Embedded
    private WorldCoord coord;

  /*@JsonBackReference
    @ManyToOne
    @JoinColumn(name="territory_id", nullable=false)
    private Territory territory;*/

    public Monster(){

    }

    public Monster(String type,int hp,int atk,int speed){
        this.m_type = type;
        this.setHp(hp);
        this.setAtk(atk);
        this.setSpeed(speed);
    }

    public Monster(Monster old_monster){
        this.m_type = old_monster.getType();
        this.setHp(old_monster.getHp());
        this.setAtk(old_monster.getAtk());
        this.setSpeed(old_monster.getSpeed());
    }

    public String getType() {
        return m_type;
    }

    public void setType(String type) {
        this.m_type = type;
    }

    public WorldCoord getCoord() { return coord; }

    public void setCoord(WorldCoord coord) { this.coord = coord; }

    public int getWid() {
        return coord.getWid();
    }

    public void setWid(int wid) {
        this.coord.setWid(wid);
    }

    public int getX() {
        return coord.getX();
    }

    public void setX(int x) {
        this.coord.setX(x);
    }

    public int getY() {
        return coord.getY();
    }

    public void setY(int y) {
        this.coord.setY(y);
    }

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

  /*public Territory getTerritory() {
    return territory;
    }

    public void setTerritory(Territory territory) {
        this.territory = territory;
    }
  */
    public JSONObject toJSON(){
        JSONObject monster_obj = new JSONObject();
//        monster_obj.put("id",id);
        monster_obj.put("type",m_type);
//        monster_obj.put("hp",hp);
//        monster_obj.put("atk",atk);
        return monster_obj;
    }
}
