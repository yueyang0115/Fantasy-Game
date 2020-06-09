package edu.duke.ece.fantasy.database;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Tile")
public class Tile {

  @EmbeddedId
  WorldCoord where;

  @Column(name = "name", nullable = false)
  private String name;

  public Tile() {
    this.where = new WorldCoord();
    this. name = "unknown";
  }

  public Tile(WorldCoord where, String name) {
    this.where = where;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public WorldCoord getWhere() {
    return where;
  }

  public void setWhere(WorldCoord where) {
    this.where = where;
  }
  @Override
  public String toString(){
    return "Tile: "+ name + " @ " + where;
  }
}
