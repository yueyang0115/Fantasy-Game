package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Unit;
import edu.duke.ece.fantasy.database.levelUp.Skill;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SkillDAO {
    private Session session;
    public SkillDAO(Session session){
        this.session = session;
    }
    public SkillDAO(){}

    public Skill getSkill(String skillName){
        Skill s = HibernateUtil.queryOne("From Skill S where S.name =:name",
                Skill.class, new String[]{"name"}, new Object[]{skillName});
        return s;
    }

    // get available skills that the unit can learn
    public Set<Skill> getAvailableSkills(Unit unit){
        List<Skill> skillList = HibernateUtil.queryList("From Skill S where S.requiredLevel <=:unitLevel",
                Skill.class, new String[]{"unitLevel"}, new Object[]{unit.getExperience().getLevel()});

        Set<Skill> skillSet = new HashSet<>();
        for(Skill skill : skillList) {
            //if unit doesn't contain that skill && unit has the required prerequisite skills
            if( !unit.getSkills().contains(skill) &&
                    (skill.getRequiredSkill() == null ||  unit.getSkills().containsAll(skill.getRequiredSkill()))) {
                skillSet.add(skill);
            }
        }
        return skillSet;
    }
}
