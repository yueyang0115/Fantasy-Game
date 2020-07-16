package edu.duke.ece.fantasy.database.levelUp;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.File;
import java.io.IOException;
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
        String filePath = getPath("ExperienceLevelTable.csv");
        String sql = "COPY ExperienceLevelEntry from "+ filePath + " WITH (FORMAT csv)";
        Query q = session.createSQLQuery(sql);
        q.executeUpdate();
    }

    public void buildLevelSkillPointTable(){
        String filePath = getPath("LevelSkillPointTable.csv");
        String sql = "COPY LevelSkillPointEntry from "+ filePath + " WITH (FORMAT csv)";
        Query q = session.createSQLQuery(sql);
        q.executeUpdate();
    }

    public String getPath(String fileName){
        String absolutePath = "";

//        try{
//            String currentDir = new File(".").getCanonicalPath();
//            String relativePath = "/src/main/resources/levelupData/";
//            absolutePath = "'" + currentDir + relativePath + fileName + "'";
//        }
//        catch(IOException e){
//            System.out.println("failed to get currentDir in table initialization");
//            e.printStackTrace();
//        }

        String currentDir = System.getProperty("user.dir");
        String relativePath = "/src/main/resources/levelupData/";
        absolutePath = "'" + currentDir + relativePath + fileName + "'";

        return absolutePath;
    }

}
