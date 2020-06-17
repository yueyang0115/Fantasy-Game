package edu.duke.ece.fantasy.database;

import javax.persistence.*;

@Entity
@Table(name = "Territory")
public class Territory {

    @EmbeddedId
    WorldCoord coord;


    @Column(name = "tame", nullable = false)
    private int tame;

    /*@JsonManagedReference
      @OneToMany(mappedBy = "territory", cascade = CascadeType.ALL)
      private List<Monster> monsters = new ArrayList<>();
    */
    private String terrainType;

    public Territory() {

    }


    public Territory(WorldCoord coord, int tame) {
        this.coord = coord;
        this.tame = tame;
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


    public Integer getTame() {
        return tame;
    }

    public void setTame(int status) {
        this.tame = status;
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
