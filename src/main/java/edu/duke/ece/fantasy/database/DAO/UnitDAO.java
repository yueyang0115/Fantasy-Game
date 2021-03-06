package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.levelUp.ExperienceLevelEntry;
import edu.duke.ece.fantasy.database.levelUp.LevelSkillPointEntry;
import edu.duke.ece.fantasy.database.levelUp.Skill;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class UnitDAO {
    private Session session;

    public UnitDAO(Session session) {
        this.session = session;
    }

    //get a unit from database based on the provided soldierID
    public Unit getUnit(int unitID) {
        Query<Unit> q = session.createQuery("From Unit U where U.id =:id",
                Unit.class);
        q.setParameter("id", unitID);
        return q.uniqueResult();
    }

    //update unit's hp
    public boolean setUnitHp(int id, int hp) {
        Unit unit = getUnit(id);
        if (unit == null) { // don't have that monster
            return false;
        }
        unit.setHp(hp);
        session.update(unit);
        return true;
    }

    //delete a unit from database
    //if unit is soldier, should call playerDAO.removeSoldier() first to remove that soldier in player
    public void deleteUnit(int unitID) {
        Unit unit;
        if ((unit = getUnit(unitID)) != null) {
            session.delete(unit);
            System.out.println("[DEBUG] Delete unit " + unit.getType() + " with ID " + unitID);
        }
    }

    // update unit's experience
    public void updateExperience(int unitID, int experience) {
        Unit unit = getUnit(unitID);
        if (unit == null || unit instanceof Monster) return;
        unit.getExperience().setExperience(experience);
        session.update(unit);
        // changes in experience may bring changes to level
        updateLevel(unit);
    }

    // update unit's level according to its experience
    private void updateLevel(Unit unit) {
        if(unit == null || unit instanceof Monster) return;
        // get the supposed unit level that corresponds to the certain experience
        Query q = session.createQuery("from ExperienceLevelEntry E where E.experience <= :unitExperience"
                +" order by E.experience DESC"
        ).setMaxResults(1);
        q.setParameter("unitExperience", unit.getExperience().getExperience());
        ExperienceLevelEntry entry = (ExperienceLevelEntry) q.uniqueResult();

        if(entry == null) return;
        // if unit's level is not updated, update it
        if(unit.getExperience().getLevel() != entry.getLevel()){
            unit.getExperience().setLevel(entry.getLevel());
            session.update(unit);
            // changes in level may bring changes to skillPoint
            updateSkillPoint(unit);
        }

    }

    // update unit's skillPoint according to its level and existing skills it has
    private void updateSkillPoint(Unit unit) {
        if(unit == null || unit instanceof Monster) return;
        Query q = session.createQuery("from LevelSkillPointEntry E where E.level <= :unitLevel"
                +" order by E.level DESC"
        ).setMaxResults(1);
        q.setParameter("unitLevel", unit.getExperience().getLevel());
        LevelSkillPointEntry entry = (LevelSkillPointEntry) q.uniqueResult();

        if(entry == null) return;
        int existingSkillNum = unit.getSkills()==null? 0 : unit.getSkills().size();
        unit.getExperience().setSkillPoint(Math.max(entry.getSkillPoint() - existingSkillNum, 0));
        session.update(unit);
    }

    // add one new skill to the unit, reduce the unit's skillPoint by one
    public boolean addSkill(int unitID, String skillName) {
        Unit unit = getUnit(unitID);
        Query<Skill> q = session.createQuery("From Skill S where S.name =:name",
                Skill.class);
        q.setParameter("name", skillName);
        Skill s = q.uniqueResult();
        int unitSkillPoint = unit.getExperience().getSkillPoint();
        if (s == null || unitSkillPoint < 1 ||
                s.getRequiredSkill() != null &&
                        (unit.getSkills() == null || !unit.getSkills().containsAll(s.getRequiredSkill()))) return false;
        if (unit.getSkills() == null || !unit.getSkills().contains(s)) {
            unit.addSkill(s);
            unit.getExperience().setSkillPoint(unitSkillPoint - 1);
            session.update(unit);
        }
        return true;

    }

    // remove one skill from a unit, add its skillPoint by one
    public boolean removeSkill(int unitID, String skillName) {
        Unit unit = getUnit(unitID);
        Query<Skill> q = session.createQuery("From Skill S where S.name =:name",
                Skill.class);
        q.setParameter("name", skillName);
        Skill s = q.uniqueResult();
        if (s == null || unit.getSkills() == null) return false;
        if (unit.getSkills().contains(s)) {
            unit.getSkills().remove(s);
            unit.getExperience().setSkillPoint(unit.getExperience().getSkillPoint() + 1);
            session.update(unit);
        }
        return true;
    }
}
