package edu.duke.ece.fantasy.task;

import edu.duke.ece.fantasy.SharedData;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.DAO.MonsterDAO;
import edu.duke.ece.fantasy.database.DAO.TerritoryDAO;
import edu.duke.ece.fantasy.json.MessagesS2C;
import edu.duke.ece.fantasy.task.MonsterMover;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.mockito.Mockito.*;


public class MonsterMoverTest {
    MetaDAO mockedMetaDAO = mock(MetaDAO.class);
    List<Monster> monsterList = new ArrayList<>();

    public MonsterMoverTest(){
        Monster m1 = new Monster("testwolf",30,3,5);
        m1.setId(2);
        m1.setCoord(new WorldCoord(1,1,1));
        monsterList.add(m1);

        MonsterDAO monsterDAO = mock(MonsterDAO.class);
        TerritoryDAO territoryDAO = mock(TerritoryDAO.class);
        when(mockedMetaDAO.getMonsterDAO()).thenReturn(monsterDAO);
        when(monsterDAO.getMonstersInRange(any(),anyInt(),anyInt())).thenReturn(monsterList);
        doNothing().when(monsterDAO).updateMonsterCoord(anyInt(),anyInt(),anyInt());
        doNothing().when(monsterDAO).setMonsterStatus(anyInt(),anyBoolean());
        when(mockedMetaDAO.getTerritoryDAO()).thenReturn(territoryDAO);
        when(territoryDAO.getTerritory(any())).thenReturn(new Territory());
    }

    @Test
    public void testAll(){
        SharedData sharedData = new SharedData();
        Player p = new Player();
        WorldInfo info = new WorldInfo();
        p.addWorldInfo(info);
        p.setStatus("mainWorld");
        p.setCurrentCoord(new WorldCoord(1,2,2));
        sharedData.setPlayer(p);
        LinkedBlockingQueue<MessagesS2C> resultMsgQueue = new LinkedBlockingQueue<>();;
        MonsterMover monsterMover = new MonsterMover(System.currentTimeMillis(), 7000, true, mockedMetaDAO, sharedData,  resultMsgQueue);
        monsterMover.doTask();
        while(resultMsgQueue.size()==0){
            monsterMover.doTask();
        }
        assertNotEquals(resultMsgQueue.peek().getPositionResultMessage().getMonsterArray().get(0).getCoord(),
                new WorldCoord(1,1,1));
    }
}
