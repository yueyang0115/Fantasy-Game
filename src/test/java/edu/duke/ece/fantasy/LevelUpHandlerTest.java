package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.DAO.PlayerDAO;
import edu.duke.ece.fantasy.database.DAO.SkillDAO;
import edu.duke.ece.fantasy.database.DAO.UnitDAO;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.Soldier;
import edu.duke.ece.fantasy.database.Unit;
import edu.duke.ece.fantasy.database.levelUp.Skill;
import edu.duke.ece.fantasy.json.*;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LevelUpHandlerTest {
    MetaDAO mockedMetaDAO = mock(MetaDAO.class);
    UnitDAO mockedUnitDAO = mock(UnitDAO.class);
    SkillDAO mockedSkillDAO = mock(SkillDAO.class);
    Soldier soldier;
    Skill miniFireBall;
    Skill middleFireBall;
    Set<Skill> availableSkills = new HashSet<>();

    public LevelUpHandlerTest(){
        this.soldier = new Soldier("wizard", 50, 5, 20);
        soldier.getExperience().setLevel(20); // set level to 20

        miniFireBall = new Skill("miniFireBall",5,10,null);
        Set<Skill> requiredSkill2 = new HashSet<>();
        requiredSkill2.add(miniFireBall);
        middleFireBall = new Skill("middleFireBall",10,20,requiredSkill2);
        availableSkills.add(miniFireBall);
        availableSkills.add(middleFireBall);

        when(mockedMetaDAO.getUnitDAO()).thenReturn(mockedUnitDAO);
        when(mockedMetaDAO.getSkillDAO()).thenReturn(mockedSkillDAO);
        when(mockedUnitDAO.getUnit(anyInt())).thenReturn(soldier);
        when(mockedSkillDAO.getAvailableSkills(any())).thenReturn(availableSkills);
    }

    @Test
    public void testAll(){
        testStart();
        testChooseSuccess();
        testChooseFail1();
        testChooseFail2();
        testChooseFail3();
        testMessageHandler();
    }

    private void testStart(){
        LevelUpHandler lh = new LevelUpHandler(mockedMetaDAO);
        LevelUpRequestMessage request = new LevelUpRequestMessage("start",1,null);
        LevelUpResultMessage result = lh.handle(request);
        assertEquals(result.getResult(),"start");
        assertEquals(result.getUnit(),(Unit) soldier);
    }

    private void testChooseSuccess(){
        when(mockedUnitDAO.addSkill(anyInt(),anyString())).thenReturn(true);

        LevelUpHandler lh = new LevelUpHandler(mockedMetaDAO);
        LevelUpRequestMessage request = new LevelUpRequestMessage("choose",1,middleFireBall);
        LevelUpResultMessage result = lh.handle(request);
        assertEquals(result.getResult(),"success");
    }

    // no required Skill
    private void testChooseFail1(){
        when(mockedUnitDAO.addSkill(anyInt(),anyString())).thenReturn(false);
        LevelUpHandler lh = new LevelUpHandler(mockedMetaDAO);

        availableSkills.remove(miniFireBall);
        soldier.getExperience().setLevel(10); // set level to 10
        LevelUpRequestMessage request = new LevelUpRequestMessage("choose",1,middleFireBall);
        LevelUpResultMessage result = lh.handle(request);
        assertEquals(result.getResult(),"fail");
        availableSkills.add(miniFireBall);
    }

    // no enough skillPoint
    private void testChooseFail2(){
        when(mockedUnitDAO.addSkill(anyInt(),anyString())).thenReturn(false);
        LevelUpHandler lh = new LevelUpHandler(mockedMetaDAO);

        soldier.getExperience().setSkillPoint(0);
        LevelUpRequestMessage request = new LevelUpRequestMessage("choose",1,middleFireBall);
        LevelUpResultMessage result = lh.handle(request);
        assertEquals(result.getResult(),"fail");
        soldier.getExperience().setSkillPoint(1);
    }

    // no corresponding skill
    private void testChooseFail3(){
        Skill fakeBall = new Skill("fakeBall",5,10,null);
        when(mockedUnitDAO.addSkill(anyInt(),anyString())).thenReturn(false);
        LevelUpHandler lh = new LevelUpHandler(mockedMetaDAO);

        LevelUpRequestMessage request = new LevelUpRequestMessage("choose",1,fakeBall);
        LevelUpResultMessage result = lh.handle(request);
        assertEquals(result.getResult(),"fail");
    }

    private void testMessageHandler(){
        when(mockedUnitDAO.addSkill(anyInt(),anyString())).thenReturn(true);
        SharedData sharedData = new SharedData();
        sharedData.setPlayer(new Player());
        MessageHandler mh = new MessageHandler(mockedMetaDAO, sharedData);
        LevelUpRequestMessage levelUpRequest = new LevelUpRequestMessage("choose",1,middleFireBall);
        MessagesC2S request = new MessagesC2S(levelUpRequest);
        MessagesS2C result = mh.handle(request);
        assertEquals(result.getLevelUpResultMessage().getResult(),"success");
    }
}

