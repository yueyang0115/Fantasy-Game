package edu.duke.ece.fantasy.database.levelUp;


import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.duke.ece.fantasy.database.Monster;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Skill")
public class Skill {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "skill_id", unique = true, nullable = false)
    private int id;

    // skill name, including "iceBall" "fireBall".....
    @Column(name = "skill_name", unique = true, nullable = false)
    private String name;

    // how much attack this skill can cause
    @Column(name = "attack", unique = false, nullable = true)
    private int attack;

    // the prerequisite level required to has this skill
    @Column(name = "requiredLevel", unique = false, nullable = true)
    private int requiredLevel;

    // the prerequisite skill required to has this skill
    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Skill_RequiredSkill",
            joinColumns = { @JoinColumn(name = "skill_id") }, //skill_id
            inverseJoinColumns = { @JoinColumn(name = "skill_name") }) //skill_name
    private Set<Skill> requiredSkill = new HashSet<>();

    public Skill(){}
    public Skill(String name, int attack) {
        this.name = name;
        this.attack = attack;
    }
    public Skill(String name, int attack, int requiredLevel, Set<Skill> requiredSkill) {
        this.name = name;
        this.attack = attack;
        this.requiredLevel = requiredLevel;
        this.requiredSkill = requiredSkill;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getRequiredLevel() { return requiredLevel; }

    public void setRequiredLevel(int requiredLevel) { this.requiredLevel = requiredLevel; }

    public Set<Skill> getRequiredSkill() { return requiredSkill; }

    public void setRequiredSkill(Set<Skill> requiredSkill) { this.requiredSkill = requiredSkill; }

    @Override
    public boolean equals(Object o) {
        if (o.getClass().equals(Skill.class)) {
            Skill s = (Skill) o;
            return this.name.equals(s.name)
                    && this.requiredLevel == s.requiredLevel
                    && this.requiredSkill.equals(s.requiredSkill)
                    && this.requiredSkill.containsAll(s.requiredSkill)
                    && this.attack == s.attack;
        }
        return false;
    }
    @Override
    public int hashCode(){
        return this.toString().hashCode();
    }
    @Override
    public String toString(){
        return (this.name + ":" + this.requiredLevel + "," + this.attack);
    }

}
