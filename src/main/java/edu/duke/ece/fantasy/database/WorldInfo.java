package edu.duke.ece.fantasy.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "WorldInfo")
public class WorldInfo {

    @Id
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
        this.worldType = "mainworld";
    }

    public WorldInfo(WorldCoord wc, String player, int tileSize) {
        wid = wc.getWid();
        startX = wc.getX();
        startY = wc.getY();
        ownerName = player;
        firstTileX = startX - tileSize / 2;
        firstTileY = startY - tileSize / 2;
        this.worldType = "mainworld";
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
