package edu.duke.ece.fantasy.database.levelUp;

import javax.persistence.Embeddable;

@Embeddable
public class Experience {
    private int experience = 0;
    private int level = 1;
    private int skillPoint = 1;

    public Experience(){}

    public Experience(int experience, int level, int skillPoint) {
        this.experience = experience;
        this.level = level;
        this.skillPoint = skillPoint;
    }

    public int getExperience() { return experience; }

    public void setExperience(int experience) { this.experience = experience; }

    public int getLevel() { return level; }

    public void setLevel(int level) { this.level = level; }

    public int getSkillPoint() { return skillPoint; }

    public void setSkillPoint(int skillPoint) { this.skillPoint = skillPoint; }
}
