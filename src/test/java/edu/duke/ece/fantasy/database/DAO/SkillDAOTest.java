package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.levelUp.Skill;
import edu.duke.ece.fantasy.database.levelUp.TableInitializer;
import org.hibernate.Session;
import org.junit.jupiter.api.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SkillDAOTest {
    public static Session session;
    private PlayerDAO playerDAO;
    private UnitDAO unitDAO;
    private SkillDAO skillDAO;
    private SoldierDAO soldierDAO;

    @BeforeAll
    public static void setUpSession(){
        session = HibernateUtil.getSessionFactory().openSession();
    }

    @AfterAll
    public static void closeSession(){
        session.close();
    }

    @BeforeEach
    public void setUp(){
        session.beginTransaction();

        playerDAO = new PlayerDAO(session);
        unitDAO = new UnitDAO(session);
        skillDAO = new SkillDAO(session);
        soldierDAO = new SoldierDAO(session);
        playerDAO.addPlayer("testname","testpassword");
    }

    @AfterEach
    public void shutDown(){
        session.getTransaction().rollback();
    }

    @Test
    public void testSkillDAO(){
        Player p = playerDAO.getPlayer("testname");
        int soldierID = soldierDAO.getSoldiers(p.getId()).get(0).getId();

        TableInitializer tableInitializer = new TableInitializer(session);
        tableInitializer.initializeAll();
        unitDAO.updateExperience(soldierID, 40);
        assertEquals(unitDAO.getUnit(soldierID).getExperience().getSkillPoint(),2);
        assertEquals(unitDAO.addSkill(soldierID, "miniFireBall"),true);
        assertEquals(unitDAO.addSkill(soldierID, "fakeTest"),false);
        assertEquals(unitDAO.addSkill(soldierID, "largeFireBall"),false);
        assertEquals(unitDAO.getUnit(soldierID).getExperience().getSkillPoint(),1);

        Set<Skill> availableSkills = new HashSet<>();
        availableSkills.add(skillDAO.getSkill("miniFireBall"));
        availableSkills.add(skillDAO.getSkill("middleFireBall"));
        assertEquals(skillDAO.getAvailableSkills(unitDAO.getUnit(soldierID)),availableSkills);

        assertEquals(unitDAO.removeSkill(soldierID,"miniFireBall"), true);
        assertEquals(unitDAO.removeSkill(soldierID,"fakeTest"), false);
        assertEquals(unitDAO.getUnit(soldierID).getExperience().getSkillPoint(),2);

        assertEquals(unitDAO.getUnit(soldierID).getSkills(),new HashSet<>());
        Set<Skill> availableSkills2 = new HashSet<>();
        availableSkills2.add(skillDAO.getSkill("miniFireBall"));
        assertEquals(skillDAO.getAvailableSkills(unitDAO.getUnit(soldierID)),availableSkills2);


    }

    @Test
    public void testSkillPoint(){
        Player p = playerDAO.getPlayer("testname");
        int soldierID = soldierDAO.getSoldiers(p.getId()).get(0).getId();
        TableInitializer tableInitializer = new TableInitializer(session);
        tableInitializer.initializeAll();

        // unit has no skill
        assertEquals(unitDAO.getUnit(soldierID).getExperience().getSkillPoint(),0);
        assertEquals(unitDAO.getUnit(soldierID).getExperience().getLevel(), 0);
        unitDAO.updateExperience(soldierID,60);
        assertEquals(unitDAO.getUnit(soldierID).getExperience().getSkillPoint(),2);
        assertEquals(unitDAO.getUnit(soldierID).getExperience().getLevel(), 14);
        unitDAO.updateExperience(soldierID,72);
        assertEquals(unitDAO.getUnit(soldierID).getExperience().getSkillPoint(),3);
        assertEquals(unitDAO.getUnit(soldierID).getExperience().getLevel(), 16);

        //unit has skill
        assertEquals(unitDAO.addSkill(soldierID, "miniFireBall"),true);
        assertEquals(unitDAO.getUnit(soldierID).getExperience().getSkillPoint(),2);
        unitDAO.updateExperience(soldierID,72);
        assertEquals(unitDAO.getUnit(soldierID).getExperience().getSkillPoint(),2);
        assertEquals(unitDAO.removeSkill(soldierID,"miniFireBall"), true);
        assertEquals(unitDAO.getUnit(soldierID).getExperience().getSkillPoint(),3);
    }
}
