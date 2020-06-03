package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.*;
import org.hibernate.Session;

import java.util.*;

public class BattleHandler {
    private Session session;
    private MonsterManger myMonsterManger;
    private SoldierManger mySoldierManger;
    Queue<Integer> speedQueue; // stores the speed of soldiers and monsters, rolls in turns

    public BattleHandler(Session session) {
        this.session = session;
        myMonsterManger = new MonsterManger(session);
        mySoldierManger = new SoldierManger(session);
    }

    public BattleResultMessage handle(BattleRequestMessage request, int playerID){
        String action = request.getAction();
        if(action.equals("escape")){
            BattleResultMessage result = new BattleResultMessage();
            result.setResult("escaped");
            return result;
        }
        else if(action.equals("start")){
            return doStart(request, playerID);
        }
        else {
            return doBattle(request, playerID);
        }
    }

    private BattleResultMessage doStart(BattleRequestMessage request, int playerID){
        BattleResultMessage result = new BattleResultMessage();
        speedQueue = new LinkedList<>();

        int territoryID = request.getTerritoryID();
        List<Monster> monsterList = myMonsterManger.getMonsters(territoryID);
        List<Soldier> soldierList = mySoldierManger.getSoldiers(playerID);
        speedQueue = getSpeedQueue(monsterList, soldierList);

        result.setMonsters(monsterList);
        result.setSoldiers(soldierList);
        result.setResult("continue");
        return result;
    }

    private Queue<Integer> getSpeedQueue(List<Monster> monsterList, List<Soldier> soldierList){
            PriorityQueue<Integer> pq = new PriorityQueue( new Comparator<Integer>() {
                public int compare(Integer e1, Integer e2) {
                    return e2 - e1;
                }
            });
            for(Monster monster : monsterList) pq.add(monster.getId());
            for(Soldier soldier : soldierList) pq.add(soldier.getId());
            Queue speedQueue = new LinkedList<>();
            while(!pq.isEmpty()) {
                speedQueue.add(pq.poll());
            }
            return speedQueue;
    }

    private BattleResultMessage doBattle(BattleRequestMessage request, int playerID){
        BattleResultMessage result = new BattleResultMessage();

        int territoryID = request.getTerritoryID();
        int monsterID = request.getMonsterID();
        int soldierID = request.getSoldierID();

        //check the monster exist in the territory
        Monster monster = myMonsterManger.getMonster(monsterID);
        if(monster == null || monster.getTerritory().getId() != territoryID){
            result.setResult("invalid");
            return result;
        }

        //set monsterList and soldierList in result
        result.setMonsters(myMonsterManger.getMonsters(territoryID));
        result.setSoldiers(mySoldierManger.getSoldiers(playerID));

        //soldier attack monster, reduce monster's hp
        Soldier soldier = mySoldierManger.getSoldier(soldierID);
        int monsterHp = monster.getHp();
        int soldierAtk = soldier.getAtk();
        int newMonsterHp = Math.max(monsterHp - soldierAtk, 0);
        myMonsterManger.setMonsterHp(monsterID,newMonsterHp);

        if(newMonsterHp == 0){
            result.setResult("win");
            myMonsterManger.deleteMonster(monsterID);
        }
        else{
            result.setResult("continue");
        }
        return result;
    }
}
