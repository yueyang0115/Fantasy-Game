package edu.duke.ece.fantasy.database.levelUp;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "LevelSkillPointEntry")
public class LevelSkillPointEntry {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "level", unique = true, nullable = false)
    private int level;

    @Column(name = "skillPoint", unique = true, nullable = false)
    private int skillPoint;

    public LevelSkillPointEntry(){}

    public LevelSkillPointEntry(int level, int skillPoint) {
        this.level = level;
        this.skillPoint = skillPoint;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getLevel() { return level; }

    public void setLevel(int level) { this.level = level; }

    public int getSkillPoint() { return skillPoint; }

    public void setSkillPoint(int skillPoint) { this.skillPoint = skillPoint; }
}
