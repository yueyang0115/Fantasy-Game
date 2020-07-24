package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.Unit;
import edu.duke.ece.fantasy.database.levelUp.Skill;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.Set;

public class SkillDAO {
    private Session session;
    public SkillDAO(Session session){
        this.session = session;
    }

    public Skill getSkill(String skillName){
        Query q = session.createQuery("From Skill S where S.name =:name");
        q.setParameter("name", skillName);
        Skill res = (Skill) q.uniqueResult();
        return res;
    }

    // get available skills that the unit can learn
    public Set<Skill> getAvailableSkills(Unit unit){
        Query q = session.createQuery("From Skill S where S.requiredLevel <=:unitLevel");
        q.setParameter("unitLevel", unit.getExperience().getLevel());
        Set<Skill> skillSet = new HashSet<>();
        for(Object o : q.list()) {
            Skill skill = (Skill) o;
            //if unit doesn't contain that skill && unit has the required prerequisite skills
            if( !unit.getSkills().contains(skill) &&
                    (skill.getRequiredSkill() == null ||  unit.getSkills().containsAll(skill.getRequiredSkill()))) {
                skillSet.add(skill);
            }
        }
        return skillSet;
    }
}
