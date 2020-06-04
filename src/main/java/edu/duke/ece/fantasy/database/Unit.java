package edu.duke.ece.fantasy.database;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;

@Entity
@Table(name="Unit")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Unit{
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "ID", unique = true, nullable = false)
    private int id;

    @Column(name = "unit_type", unique = false, nullable = false, length = 100)
    private String u_type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return u_type;
    }

    public void setType(String type) {
        this.u_type = type;
    }
}
