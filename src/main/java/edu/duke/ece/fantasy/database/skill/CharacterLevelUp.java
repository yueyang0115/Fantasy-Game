package edu.duke.ece.fantasy.database.skill;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "CharacterLevelUp")
public class CharacterLevelUp {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "character_id", unique = true, nullable = false)
    private int id;

    //character name, including "wizard" "soldier"...
    @Column(name = "name", unique = false, nullable = false)
    private String name;

    // character level
    @Column(name = "level", unique = false, nullable = true)
    private int level;

    //character's newly added skills in that level
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "Character_Skill",
            joinColumns = { @JoinColumn(name = "character_id") },
            inverseJoinColumns = { @JoinColumn(name = "skill_name") })
    private Set<Skill> skills = new HashSet<>();

    public CharacterLevelUp(String name, int level, Set<Skill> skills) {
        this.name = name;
        this.level = level;
        this.skills = skills;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }
}
