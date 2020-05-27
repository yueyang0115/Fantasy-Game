package edu.duke.ece.fantasy.database;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table( name = "Terrain" )
public class Terrain {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "ID", unique = true, nullable = false)
    private int id;

    @Column(name = "TYPE", unique = false, nullable = false, length = 100)
    private String type;

    @OneToMany(mappedBy = "terrain",cascade = CascadeType.ALL)
    private List<Territory> territories = new ArrayList<>();

    public Terrain() {
    }

    public Terrain(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addTerritory(Territory territory){
        this.territories.add(territory);
    }


}
