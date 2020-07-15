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
        buildSkillTable();
        buildLevelSkillPointTable();
        buildExperienceLevelTable();
    }

    public void buildSkillTable(){
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

    public void buildExperienceLevelTable(){
        String filename = "'/Users/yueyang/IdeaProjects/fantasy/src/main/resources/levelupData/ExperienceLevelTable.csv'";
        String sql = "COPY ExperienceLevelEntry from "+ filename + " WITH (FORMAT csv)";
        Query q = session.createSQLQuery(sql);
        q.executeUpdate();
    }

    public void buildLevelSkillPointTable(){
        String filename = "'/Users/yueyang/IdeaProjects/fantasy/src/main/resources/levelupData/LevelSkillPointTable.csv'";
        String sql = "COPY LevelSkillPointEntry from "+ filename + " WITH (FORMAT csv)";
        Query q = session.createSQLQuery(sql);
        q.executeUpdate();
    }

}
