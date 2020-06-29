package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BattleHandlerTest {
    List<Monster> monsterList = new ArrayList<>();
    List<Soldier> soldierList = new ArrayList<>();
    BattleHandler bh = new BattleHandler();
    MetaDAO mockedMetaDAO;

    public BattleHandlerTest(){
        Monster m1 = new Monster("testwolf",3,3,5);
        m1.setId(2);
        monsterList.add(m1);
        Soldier s1 = new Soldier("soldier", 50, 5, 20);
        s1.setId(1);
        Soldier s2 = new Soldier("soldier", 50, 5, 1);
        s2.setId(3);
        soldierList.add(s1);
        soldierList.add(s2);

        mockedMetaDAO = mock(MetaDAO.class);
        MonsterDAO mockedMonsterDAO = mock(MonsterDAO.class);
        SoldierDAO mockedSoldierDAO = mock(SoldierDAO.class);
        UnitDAO mockedUnitDAO = mock(UnitDAO.class);
        PlayerDAO mockedPlayerDAO = mock(PlayerDAO.class);

        when(mockedMetaDAO.getMonsterDAO()).thenReturn(mockedMonsterDAO);
        when(mockedMetaDAO.getSoldierDAO()).thenReturn(mockedSoldierDAO);
        when(mockedMetaDAO.getUnitDAO()).thenReturn(mockedUnitDAO);
        when(mockedMetaDAO.getPlayerDAO()).thenReturn(mockedPlayerDAO);

        when(mockedMonsterDAO.getMonsters(any())).thenReturn(monsterList);
        when(mockedSoldierDAO.getSoldiers(anyInt())).thenReturn(soldierList);
        when(mockedUnitDAO.getUnit(1)).thenReturn(s1);
        when(mockedUnitDAO.getUnit(2)).thenReturn(m1);
        when(mockedUnitDAO.getUnit(3)).thenReturn(s2);

        when(mockedUnitDAO.setUnitHp(anyInt(),anyInt())).thenReturn(true);
        doNothing().when(mockedUnitDAO).deleteUnit(anyInt());
    }


    @Test
    public void testAll(){
        generateUnitQueueTest();
        generateIDListTest();
        rollUnitQueueTest();
        testStart();
        testBattle();
    }

    public void generateUnitQueueTest(){
        Queue<Unit> q = bh.generateUnitQueue(monsterList,soldierList);
        assertEquals(q.poll().getSpeed(),20);
        assertEquals(q.poll().getSpeed(),5);
        assertEquals(q.poll().getSpeed(),1);
    }
    public void generateIDListTest(){
        Queue<Unit> q = bh.generateUnitQueue(monsterList,soldierList);
        List<Integer> unitIDList= bh.generateIDList(q);
        assertEquals(unitIDList.get(0),1);
        assertEquals(unitIDList.get(1),2);
        assertEquals(unitIDList.get(2),3);

    }
    public void rollUnitQueueTest(){
        Queue<Unit> q = bh.generateUnitQueue(monsterList,soldierList);
        q = bh.rollUnitQueue(q,2);
        assertEquals(q.poll().getSpeed(),1);
        assertEquals(q.poll().getSpeed(),20);
    }

    public void testStart(){

    }

    public void testBattle(){

    }
}
