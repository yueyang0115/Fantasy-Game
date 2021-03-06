package edu.duke.ece.fantasy.database;

import edu.duke.ece.fantasy.database.levelUp.Experience;
import edu.duke.ece.fantasy.database.levelUp.Skill;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Unit")
@Inheritance(strategy = InheritanceType.JOINED)
public class Unit {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "unit_id", unique = true, nullable = false)
    private int id;

    // the type of unit, either "monster" or "soldier"
    @Column(name = "type", unique = false, nullable = false, length = 100)
    private String type;

    // the name of unit, including "wolf" "wizard"...
    @Column(name = "name", unique = false, nullable = false)
    private String name;

    // experience includes level, experience and skillPoint of the unit
    @Embedded
    private Experience experience = new Experience();

    @Column(name = "HP", unique = false, nullable = false)
    private int hp;

    @Column(name = "ATK", unique = false, nullable = false)
    private int atk;

    @Column(name = "speed", unique = false, nullable = false)
    private int speed;

//    @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL)
//    private List<UnitEquipment> equipments = new ArrayList<>();
    private DBItem weapon;


    // the skills the unit has
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Unit_Skill",
            joinColumns = { @JoinColumn(name = "unit_id") }, //unit_id
            inverseJoinColumns = { @JoinColumn(name = "skill_name") }) //skill_name
    private Set<Skill> skills = new HashSet<>();

    public Unit() {
    }

    public Unit(Unit unit) {
        this.id = unit.getId();
        this.name = unit.getName();
        this.type = unit.getType();
        this.hp = unit.getHp();
        this.atk = unit.getAtk();
        this.speed = unit.getSpeed();
        this.skills = unit.getSkills();
        this.experience = unit.getExperience();
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

    public void addSpeed(int speed) {
        this.speed += speed;
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

    public void addAtk(int atk) {
        this.atk += atk;
    }

    public void reduceAtk(int atk){
        this.atk -= atk;
    }

    public void reduceSpeed(int speed){
        this.speed -= speed;
    }

    public void reduceHp(int hp){
        this.hp -= hp;
    }


    public DBItem getWeapon() {
        return weapon;
    }

    public void setWeapon(DBItem weapon) {
        this.weapon = weapon;
    }

    //    public boolean addEquipment(UnitEquipment equipment) {
//        equipments.add(equipment);
//        return true;

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    public void addSkill(Skill skill){
        skills.add(skill);
    }

    public Experience getExperience() { return experience; }

    public void setExperience(Experience experience) { this.experience = experience; }


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
}
