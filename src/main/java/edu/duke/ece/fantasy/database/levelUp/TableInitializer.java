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

//        for(int i=1; i<=5; i++){
//            ExperienceLevelEntry el1 = new ExperienceLevelEntry(3*i,i); // exp: 15 level: 5
//            session.save(el1);
//        }
//        for(int i=1; i<=10; i++) {
//            ExperienceLevelEntry el2 = new ExperienceLevelEntry(15 + 5 * i, 5 + i); // exp: 20 level: 6
//            session.save(el2);
//        }
//        for(int i=1; i<=15; i++){
//            ExperienceLevelEntry el3 = new ExperienceLevelEntry(65 + 7 * i, 15 + i);
//            session.save(el3);
//        }
    }

    public void buildLevelSkillPointTable(){
        LevelSkillPointEntry lsp1 = new LevelSkillPointEntry(1,1);
        LevelSkillPointEntry lsp2 = new LevelSkillPointEntry(5,2);
        LevelSkillPointEntry lsp3 = new LevelSkillPointEntry(15,3);
        LevelSkillPointEntry lsp4 = new LevelSkillPointEntry(30,4);
        session.save(lsp1);
        session.save(lsp2);
        session.save(lsp3);
        session.save(lsp4);
    }

}
