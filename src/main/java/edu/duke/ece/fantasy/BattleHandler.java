package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.*;
import org.hibernate.Session;

import java.util.*;

public class BattleHandler {
    private Session session;
    private MonsterManger myMonsterManger;
    private SoldierManger mySoldierManger;
    Queue<Unit> speedQueue; // stores the speed of soldiers and monsters, rolls in turns

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

    public BattleResultMessage doStart(BattleRequestMessage request, int playerID){
        BattleResultMessage result = new BattleResultMessage();
        speedQueue = new LinkedList<>();

        int territoryID = request.getTerritoryID();
        List<Monster> monsterList = myMonsterManger.getMonsters(territoryID);
        List<Soldier> soldierList = mySoldierManger.getSoldiers(playerID);
        speedQueue = generateSpeedQueue(monsterList, soldierList);
        List<Integer> unitIDList = genarateIDList(speedQueue);

        result.setMonsters(monsterList);
        result.setSoldiers(soldierList);
        result.setResult("continue");
        result.setUnitIDs(unitIDList);

        return result;
    }

    public Queue<Unit> generateSpeedQueue(List<Monster> monsterList, List<Soldier> soldierList){
            PriorityQueue<Unit> pq = new PriorityQueue( new Comparator<Unit>() {
                public int compare(Unit u1, Unit u2) {
                    return u2.getSpeed() - u1.getSpeed();
                }
            });
            for(Monster monster : monsterList) pq.add(monster);
            for(Soldier soldier : soldierList) pq.add(soldier);
            Queue<Unit> speedQueue = new LinkedList<>();
            while(!pq.isEmpty()) {
                speedQueue.add(pq.poll());
            }
            return speedQueue;
    }

    public List<Integer> genarateIDList(Queue<Unit> q){
        Unit[] unitArray = (Unit[]) q.toArray();
        List<Integer> unitIDList = new ArrayList<>();
        for(Unit u : unitArray) unitIDList.add(u.getId());
        return unitIDList;
    }

    public BattleResultMessage doBattle(BattleRequestMessage request, int playerID){
        BattleResultMessage result = new BattleResultMessage();

        int territoryID = request.getTerritoryID();
        int monsterID = request.getAttackeeID();
        int soldierID = request.getAttackerID();

        //check the monster exist in the territory
        Monster monster = myMonsterManger.getMonster(monsterID);
        if(monster == null || monster.getTerritory().getId() != territoryID){
            result.setResult("invalid");
            return result;
        }

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
