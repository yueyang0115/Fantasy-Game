package edu.duke.ece.fantasy.database;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;
import org.json.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Territory")
public class Territory {

    @EmbeddedId
    WorldCoord coord;


    @Column(name = "status", nullable = false)
    private int status;

    /*@JsonManagedReference
      @OneToMany(mappedBy = "territory", cascade = CascadeType.ALL)
      private List<Monster> monsters = new ArrayList<>();
    */
    private String terrainType;

    public Territory() {

    }


    public Territory(WorldCoord coord, int status) {
        this.coord = coord;
        this.status = status;
    }


    public String getTerrainType() {
        return terrainType;
    }

    public void setTerrainType(String terrainType) {
        this.terrainType = terrainType;
    }

    public WorldCoord getCoord() {
        return coord;
    }

    public void setCoord(WorldCoord coord) {
        this.coord = coord;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

  /*public void addMonster(Monster monster) {
      //monster.setTerritory(this);
        this.monsters.add(monster);
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(List<Monster> monsters) {
        this.monsters = monsters;
        }*/

}
