package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.Unit;
import edu.duke.ece.fantasy.database.levelUp.Skill;
import org.hibernate.Session;
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

    public void UpdateLevel(int unitID, int level){
        Unit unit = getUnit(unitID);
        unit.setLevel(level);
        session.update(unit);
    }

    public boolean addSkill(int unitID, String skillName){
        Unit unit = getUnit(unitID);
        Query q = session.createQuery("From Skill S where S.name =:name");
        q.setParameter("name", skillName);
        Skill s = (Skill) q.uniqueResult();
        if(s == null) return false;
        else{
            unit.addSkill(s);
            session.update(unit);
            return true;
        }
    }

    public boolean removeSkill(int unitID, String skillName){
        Unit unit = getUnit(unitID);
        Query q = session.createQuery("From Skill S where S.name =:name");
        q.setParameter("name", skillName);
        Skill s = (Skill) q.uniqueResult();
        if(s!= null && unit.getSkills().contains(s)) {
            unit.getSkills().remove(s);
            session.update(unit);
            return true;
        }
        return false;
    }
}
