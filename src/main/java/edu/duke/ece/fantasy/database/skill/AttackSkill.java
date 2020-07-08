package edu.duke.ece.fantasy.database.skill;

public class AttackSkill extends Skill {
    private String name;
    private int attack;

    public AttackSkill(String name, int attack) {
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
