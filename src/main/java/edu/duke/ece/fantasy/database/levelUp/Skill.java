package edu.duke.ece.fantasy.database.levelUp;


import edu.duke.ece.fantasy.database.Monster;

import javax.persistence.*;

@Entity
@Table(name = "Skill")
public class Skill {

    // skill name, including "iceBall" "fireBall".....
    @Id
    @Column(name = "skill_name", unique = true, nullable = false)
    private String name;

    // how much attack this skill can cause
    @Column(name = "attack", unique = false, nullable = true)
    private int attack;

    // the prerequisite level required to has this skill
    @Column(name = "level", unique = false, nullable = true)
    private int requiredLevel;

    // the prerequisite skill required to has this skill
    @ManyToOne
    @JoinColumn(name = "prerequisite", unique = false, nullable = true)
    private Skill requiredSkill;

    public Skill(){}
    public Skill(String name, int attack) {
        this.name = name;
        this.attack = attack;
    }
    public Skill(String name, int attack, int requiredLevel, Skill requiredSkill) {
        this.name = name;
        this.attack = attack;
        this.requiredLevel = requiredLevel;
        this.requiredSkill = requiredSkill;
    }

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

    public Skill getRequiredSkill() { return requiredSkill; }

    public void setRequiredSkill(Skill requiredSkill) { this.requiredSkill = requiredSkill; }

    @Override
    public boolean equals(Object o) {
        if (o.getClass().equals(Skill.class)) {
            Skill s = (Skill) o;
            return this.name.equals(s.name)
                    && this.requiredLevel == s.requiredLevel
                    && this.requiredSkill.equals(s.requiredSkill)
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
