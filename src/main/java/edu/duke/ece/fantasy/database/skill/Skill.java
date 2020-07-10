package edu.duke.ece.fantasy.database.skill;


import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.duke.ece.fantasy.database.Unit;
import org.hibernate.annotations.GenericGenerator;

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

    public Skill(){}

    public Skill(String name, int attack) {
        this.name = name;
        this.attack = attack;
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
}
