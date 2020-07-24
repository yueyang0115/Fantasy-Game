package edu.duke.ece.fantasy.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@Entity
@Table(name = "WorldInfo")
public class WorldInfo implements Serializable {
//    public enum WorldType {
//        Main, Death;
//    }
    public static String MainWorld = "mainWorld";

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "WID", unique = true, nullable = false)
    private int wid;

    @Column(name = "name", nullable = false)
    private String ownerName; //what player owns this world

    //starting x,y coordinates for world
    private int startX;
    private int startY;

    private int firstTileX;
    private int firstTileY;

    private String worldType;

    public WorldInfo() {
        this.worldType = MainWorld;
    }

    public WorldInfo(WorldCoord wc, String player, int tileSize) {
//        wid = wc.getWid();
        startX = wc.getX();
        startY = wc.getY();
        ownerName = player;
        firstTileX = startX - tileSize / 2;
        firstTileY = startY - tileSize / 2;
        this.worldType = MainWorld;
    }

    public int getWid() {
        return wid;
    }

    public WorldCoord getStartCoords() {
        return new WorldCoord(wid, startX, startY);
    }

    public WorldCoord getFirstTile() {
        return new WorldCoord(wid, firstTileX, firstTileY);
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getWorldType() {
        return this.worldType;
    }

    public void setWorldType(String worldType) { this.worldType = worldType; }

}
