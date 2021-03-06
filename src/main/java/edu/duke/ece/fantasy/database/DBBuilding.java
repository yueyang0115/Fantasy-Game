package edu.duke.ece.fantasy.database;

import edu.duke.ece.fantasy.Building.Prototype.Building;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "Building")
public class DBBuilding {
//    @Id
//    @GeneratedValue(generator = "increment")
//    @GenericGenerator(name = "increment", strategy = "increment")
//    @Column(name = "ID", unique = true, nullable = false)
//    private int id;

    @EmbeddedId
    private WorldCoord coord;

    @Column(name = "class_name")
    private String name;

    public DBBuilding() {
    }

    public DBBuilding(String name) {
        this.name = name;
    }

    public DBBuilding(String name, WorldCoord coord) {
        this.name = name;
        this.coord = coord;
    }

    public Building toGameBuilding() {
        try {
            Building res = (Building) Class.forName(name).getDeclaredConstructor().newInstance();
            res.setCoord(coord);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public WorldCoord getCoord() {
        return coord;
    }

    public void setCoord(WorldCoord coord) {
        this.coord = coord;
    }

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
