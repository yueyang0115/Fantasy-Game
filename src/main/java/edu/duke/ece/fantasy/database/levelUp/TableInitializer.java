package edu.duke.ece.fantasy.database.levelUp;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Monster;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.query.Query;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TableInitializer {
    private Session session;
    private Connection connection;

    public TableInitializer(Session session, Connection connection) {
        this.session = session;
        this.connection = connection;
    }

    public void initializeAll() {
        Query<Skill> q = session.createQuery("From Skill",
                Skill.class);
        List<Skill> skillList = q.getResultList();

        if (skillList == null || skillList.size() == 0) {
            buildSkillTable();
        }

        Query<LevelSkillPointEntry> q2 = session.createQuery("From LevelSkillPointEntry", LevelSkillPointEntry.class);
        List<LevelSkillPointEntry> entryList1 = q2.getResultList();
        if (entryList1 == null || entryList1.size() == 0) {
            buildLevelSkillPointTable();
        }

        Query<ExperienceLevelEntry> q3 = session.createQuery("From ExperienceLevelEntry", ExperienceLevelEntry.class);
        List<ExperienceLevelEntry> entryList2 = q3.getResultList();
        if (entryList2 == null || entryList2.size() == 0) {
            buildExperienceLevelTable();
        }
    }

    public void buildSkillTable() {
        Skill miniFireBall = new Skill("miniFireBall", 5, 1, null);
        Set<Skill> requiredSkill2 = new HashSet<>();
        requiredSkill2.add(miniFireBall);
        Skill middleFireBall = new Skill("middleFireBall", 10, 5, requiredSkill2);
        Set<Skill> requiredSkill3 = new HashSet<>();
        requiredSkill3.add(middleFireBall);
        Skill largeFireBall = new Skill("largeFireBall", 15, 15, requiredSkill3);

        session.save(miniFireBall);
        session.save(middleFireBall);
        session.save(largeFireBall);
    }

    public void buildExperienceLevelTable() {
        csvReader("ExperienceLevelEntry", "/levelupData/ExperienceLevelTable.csv");
    }

    public void buildLevelSkillPointTable() {
        csvReader("LevelSkillPointEntry", "/levelupData/LevelSkillPointTable.csv");
    }

    public void csvReader(String tableName, String fileName) {
        try {
            CopyManager copyManager = new CopyManager((BaseConnection) connection);
            InputStream inputStream = getClass().getResourceAsStream(fileName);
            if (inputStream == null) {
                System.out.println("input Stream is null");
            }
            String sql = "COPY " + tableName + " FROM STDIN WITH (FORMAT csv)";
            copyManager.copyIn(sql, inputStream);
            connection.commit();
        } catch (IOException | SQLException e) {
            System.out.println("csvReader failed to get File " + fileName);
            e.printStackTrace();
        }
    }

}
