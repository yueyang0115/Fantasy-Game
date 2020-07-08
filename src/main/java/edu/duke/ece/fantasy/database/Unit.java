package edu.duke.ece.fantasy.database;

import edu.duke.ece.fantasy.database.skill.Skill;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Unit")
@Inheritance(strategy = InheritanceType.JOINED)
public class Unit {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "ID", unique = true, nullable = false)
    private int id;

    @Column(name = "type", unique = false, nullable = false, length = 100)
    private String type;

    @Column(name = "name", unique = false, nullable = false)
    private String name;

    @Column(name = "HP", unique = false, nullable = false)
    private int hp;

    @Column(name = "ATK", unique = false, nullable = false)
    private int atk;

    @Column(name = "speed", unique = false, nullable = false)
    private int speed;

    @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL)
    private List<UnitEquipment> equipments = new ArrayList<>();

    public Unit() {
    }

    public Unit(Unit unit) {
        this.id = unit.getId();
        this.name = unit.getName();
        this.type = unit.getType();
        this.hp = unit.getHp();
        this.atk = unit.getAtk();
        this.speed = unit.getSpeed();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void addHp(int heal_hp) {
        hp += heal_hp;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

//    public List<Skill> getSkills() {
//        return skills;
//    }
//
//    public void setSkills(List<Skill> skills) {
//        this.skills = skills;
//    }
//
//    public void addSkill(Skill skill){
//        skills.add(skill);
//    }

//    public boolean addEquipment(ItemPack equipment) {
//        int ind = equipments.indexOf(equipment);
//        if (ind == -1) {
//            equipments.add(equipment);
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public List<ItemPack> getEquipment() {
//        return equipments;
//    }
//
//    public void setEquipment(List<ItemPack> equipment) {
//        this.equipments = equipment;
//    }
}
