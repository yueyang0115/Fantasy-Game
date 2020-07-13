package edu.duke.ece.fantasy.database.levelUp;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.Set;

public class TableInitializer {
    private Session session;

    public TableInitializer(Session session){
        this.session = session;
    }

    public void initializeAll(){
        initializeSkillTable();
        initializeSkillPointTable();
    }

    public void initializeSkillTable(){
        Skill miniFireBall = new Skill("miniFireBall",5,1,null);
        Set<Skill> requiredSkill2 = new HashSet<>();
        requiredSkill2.add(miniFireBall);
        Skill middleFireBall = new Skill("middleFireBall",10,5,requiredSkill2);
        Set<Skill> requiredSkill3 = new HashSet<>();
        requiredSkill3.add(middleFireBall);
        Skill largeFireBall = new Skill("largeFireBall",15,15,requiredSkill3);

        session.save(miniFireBall);
        session.save(middleFireBall);
        session.save(largeFireBall);
    }

    public void initializeSkillPointTable(){
        SkillPoint sk1 = new SkillPoint(1,1);
        SkillPoint sk2 = new SkillPoint(5,2);
        SkillPoint sk3 = new SkillPoint(15,3);
        SkillPoint sk4 = new SkillPoint(30,4);
        session.save(sk1);
        session.save(sk2);
        session.save(sk3);
        session.save(sk4);
    }

//    public void initializeLevelUpTable(){
//        Set<Skill> levelW1Skills = new HashSet<>();
//        levelW1Skills.add(getSkill("ironBall"));
//        CharacterLevelUp levelUpW1 = new CharacterLevelUp("wizard",10,levelW1Skills);
//
//        Set<Skill> levelW2Skills = new HashSet<>();
//        levelW2Skills.add(getSkill("iceBall"));
//        CharacterLevelUp levelUpW2 = new CharacterLevelUp("wizard",20,levelW2Skills);
//
//        Set<Skill> levelW3Skills = new HashSet<>();
//        levelW3Skills.add(getSkill("fireBall"));
//        CharacterLevelUp levelUpW3 = new CharacterLevelUp("wizard",30,levelW3Skills);
//
//        session.save(levelUpW1);
//        session.save(levelUpW2);
//        session.save(levelUpW3);
//    }
//
//    public Skill getSkill(String skillName){
//        Query q = session.createQuery("From Skill S where S.name =:name");
//        q.setParameter("name", skillName);
//        Skill s = (Skill) q.uniqueResult();
//        return s;
//    }
}
