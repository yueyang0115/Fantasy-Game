package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Soldier;
import edu.duke.ece.fantasy.database.Unit;
import edu.duke.ece.fantasy.database.levelUp.Skill;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SkillDAO {
    private Session session;

    public SkillDAO(Session session) {
        this.session = session;
    }

    public SkillDAO() {
    }

    public Skill getSkill(String skillName) {
        Query<Skill> q = session.createQuery("From Skill S where S.name =:name",
                Skill.class);
        q.setParameter("name", skillName);
        return q.uniqueResult();
    }

    // get available skills that the unit can learn
    public Set<Skill> getAvailableSkills(Unit unit) {
        Query<Skill> q = session.createQuery("From Skill S where S.requiredLevel <=:unitLevel",
                Skill.class);
        q.setParameter("unitLevel", unit.getExperience().getLevel());
        List<Skill> skillList = q.getResultList();
        Set<Skill> skillSet = new HashSet<>();
        for (Skill skill : skillList) {
            //if unit doesn't contain that skill && unit has the required prerequisite skills
            if (!unit.getSkills().contains(skill) &&
                    (skill.getRequiredSkill() == null || unit.getSkills().containsAll(skill.getRequiredSkill()))) {
                skillSet.add(skill);
            }
        }
        return skillSet;
    }
}
