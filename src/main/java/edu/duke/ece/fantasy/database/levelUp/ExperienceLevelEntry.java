package edu.duke.ece.fantasy.database.levelUp;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "ExperienceLevelEntry")
public class ExperienceLevelEntry {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "experience", unique = true, nullable = false)
    private int experience;

    @Column(name = "level", unique = true, nullable = false)
    private int level;

    public ExperienceLevelEntry(){}

    public ExperienceLevelEntry(int experience, int level) {
        this.experience = experience;
        this.level = level;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getExperience() { return experience; }

    public void setExperience(int experience) { this.experience = experience; }

    public int getLevel() { return level; }

    public void setLevel(int level) { this.level = level; }

}
