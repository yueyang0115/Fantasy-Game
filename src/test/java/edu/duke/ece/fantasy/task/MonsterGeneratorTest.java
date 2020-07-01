package edu.duke.ece.fantasy.task;

import edu.duke.ece.fantasy.SharedData;
import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.DAO.MonsterDAO;
import edu.duke.ece.fantasy.database.DAO.TerritoryDAO;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.Territory;
import edu.duke.ece.fantasy.database.WorldCoord;
import edu.duke.ece.fantasy.json.MessagesS2C;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class MonsterGeneratorTest {
    MetaDAO mockedMetaDAO = mock(MetaDAO.class);

    public MonsterGeneratorTest(){

        MonsterDAO monsterDAO = mock(MonsterDAO.class);
        TerritoryDAO territoryDAO = mock(TerritoryDAO.class);
        when(mockedMetaDAO.getMonsterDAO()).thenReturn(monsterDAO);
        when(mockedMetaDAO.getTerritoryDAO()).thenReturn(territoryDAO);
        when(monsterDAO.countMonstersInRange(any(),anyInt(),anyInt())).thenReturn((long) 2);
        doNothing().when(monsterDAO).addMonster(any(),any());
        when(territoryDAO.getWildestCoordInRange(any(),anyInt(),anyInt())).thenReturn(new WorldCoord(1,2,2));
        when(territoryDAO.getTerritory(any())).thenReturn(new Territory());
    }

    @Test
    public void testAll(){
        SharedData sharedData = new SharedData();
        Player p = new Player();
        p.setStatus(Player.Status.INMAIN);
        p.setCurrentCoord(new WorldCoord(1,1,1));
        sharedData.setPlayer(p);
        LinkedBlockingQueue<MessagesS2C> resultMsgQueue = new LinkedBlockingQueue<>();;
        MonsterGenerator monsterGenerator = new MonsterGenerator(System.currentTimeMillis(), 1000, true, mockedMetaDAO, sharedData, resultMsgQueue);
        monsterGenerator.doTask();
        assertNotNull(resultMsgQueue.peek().getPositionResultMessage().getMonsterArray());
        assertEquals(resultMsgQueue.peek().getPositionResultMessage().getMonsterArray().get(0).getCoord(),new WorldCoord(1,2,2));
    }
}
