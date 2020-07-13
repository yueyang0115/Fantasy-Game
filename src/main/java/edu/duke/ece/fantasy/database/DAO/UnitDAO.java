package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.Unit;
import edu.duke.ece.fantasy.database.levelUp.Skill;
import edu.duke.ece.fantasy.database.levelUp.SkillPoint;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.Set;

public class UnitDAO {
    private Session session;

    public UnitDAO(Session session) {
        this.session = session;
    }

    //get a unit from database based on the provided soldierID
    public Unit getUnit(int unitID) {
        Query q = session.createQuery("From Unit U where U.id =:id");
        q.setParameter("id", unitID);
        Unit res = (Unit) q.uniqueResult();
        return res;
    }

    //update unit's hp
    public boolean setUnitHp(int id, int hp){
        Unit unit = getUnit(id);
        if (unit == null) { // don't have that monster
            return false;
        }
        unit.setHp(hp);
        session.update(unit);
        return true;
    }

    //delete a unit from database
    public void deleteUnit(int unitID){
        Unit unit;
        if ((unit = (Unit) session.get(Unit.class, unitID)) != null) {
            session.delete(unit);
            System.out.println("[DEBUG] Delete unit "+unit.getType() +" with ID " +unitID);
        }
    }

    // update unit's level and change its skillPoint according to the skillPoint table
    public void updateLevel(int unitID, int level){
        Unit unit = getUnit(unitID);
        unit.setLevel(level);
        session.update(unit);
        updateSkillPoint(unit);
    }

    // update unit's skillPoint according to its level and existing skills it has
    private void updateSkillPoint(Unit unit){
        Query q1 = session.createQuery("from SkillPoint SP where SP.level <= :unitLevel"
                +" order by SP.level DESC"
        ).setMaxResults(1);
        q1.setParameter("unitLevel", unit.getLevel());
        SkillPoint sp = (SkillPoint) q1.uniqueResult();

        int existingSkillNum = unit.getSkills()==null? 0 : unit.getSkills().size();
        unit.setSkillPoint(Math.max(sp.getSkillPoint() - existingSkillNum, 0));
        session.update(unit);
    }

    // add one new skill to the unit, reduce the unit's skillPoint by one
    public boolean addSkill(int unitID, String skillName){
        Unit unit = getUnit(unitID);
        Query q = session.createQuery("From Skill S where S.name =:name");
        q.setParameter("name", skillName);
        Skill s = (Skill) q.uniqueResult();

        if(s == null || unit.getSkillPoint() < 1 ||
                s.getRequiredSkill() != null &&
                        (unit.getSkills() == null || !unit.getSkills().containsAll(s.getRequiredSkill()))) return false;
        if(unit.getSkills() == null || !unit.getSkills().contains(s)){
            unit.addSkill(s);
            unit.setSkillPoint(unit.getSkillPoint()-1);
            session.update(unit);
        }
        return true;

    }

    // remove one skill from a unit, add its skillPoint by one
    public boolean removeSkill(int unitID, String skillName){
        Unit unit = getUnit(unitID);
        Query q = session.createQuery("From Skill S where S.name =:name");
        q.setParameter("name", skillName);
        Skill s = (Skill) q.uniqueResult();
        if(s == null || unit.getSkills() == null) return false;
        if(unit.getSkills().contains(s)) {
            unit.getSkills().remove(s);
            unit.setSkillPoint(unit.getSkillPoint()+1);
            session.update(unit);
        }
        return true;
    }
}
