package edu.duke.ece.fantasy.database;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable

public class WorldCoord implements Serializable{

  private int wid;

  private int x;

  private int y;


  public WorldCoord() {
    this.wid = -1; //invalid world id
    this.x = -999999; //fairly distinctive pattern
    this.y = 999999; //in case we accidently save one unitialized
  }
  public WorldCoord(int wid, int x, int y) {
    this.wid = wid;
    this.x=x;
    this.y=y;
  }
    
  
  public int getWid() {
    return wid;
  }

  public void setWid(int wid) {
    this.wid = wid;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }
  @Override
  public boolean equals(Object o) {
    if (o.getClass().equals(WorldCoord.class)) {
      WorldCoord wc = (WorldCoord) o;
      return x==wc.x && y==wc.y && wid == wc.wid;
    }
    return false;
  }
  @Override
  public int hashCode(){
    return this.toString().hashCode();
  }
  @Override
  public String toString(){
    return (wid + ":" + x + "," + y);
  }
}
