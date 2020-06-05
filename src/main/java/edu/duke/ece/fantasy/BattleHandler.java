package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.*;
import org.hibernate.Session;

import java.util.*;

public class BattleHandler {
    private Session session;
    private MonsterManger myMonsterManger;
    private SoldierManger mySoldierManger;
    private UnitManager myUnitManager;

    /* unitQueue: keep track of the unit's attack order, it is first sorted by unit's speed
    the units will take turns to attack in the order of the queue,
    once a unit finishes attack, it will be added to the back of the queue */
    private Queue<Unit> unitQueue;

    public BattleHandler(Session session) {
        this.session = session;
        myMonsterManger = new MonsterManger(session);
        mySoldierManger = new SoldierManger(session);
        myUnitManager = new UnitManager(session);
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

    // handle "start battle" message, generate a new unitQueue to keep track of the attacker order
    public BattleResultMessage doStart(BattleRequestMessage request, int playerID){
        // create a new unitQueue each time a new battle starts
        this.unitQueue = new LinkedList<>();
        BattleResultMessage result = new BattleResultMessage();

        // get monsters and soldiers engaged in the battle
        int territoryID = request.getTerritoryID();
        List<Monster> monsterList = myMonsterManger.getMonsters(territoryID);
        List<Soldier> soldierList = mySoldierManger.getSoldiers(playerID);
        // sort unit by speed and set the UnitQueue
        this.unitQueue = generateUnitQueue(monsterList, soldierList);
        // make a list of unitIDs, corresponding units of these IDs are in same order with unitQueue
        List<Integer> unitIDList = generateIDList(unitQueue);

        result.setMonsters(monsterList);
        result.setSoldiers(soldierList);
        result.setResult("continue");
        result.setUnitIDs(unitIDList);
        return result;
    }

    //sort units by speed and set the UnitQueue
    public Queue<Unit> generateUnitQueue(List<Monster> monsterList, List<Soldier> soldierList){
            PriorityQueue<Unit> pq = new PriorityQueue( new Comparator<Unit>() {
                public int compare(Unit u1, Unit u2) {
                    return u2.getSpeed() - u1.getSpeed();
                }
            });
            for(Monster monster : monsterList) pq.add(monster);
            for(Soldier soldier : soldierList) pq.add(soldier);
            Queue<Unit> q = new LinkedList<>();
            while(!pq.isEmpty()) {
                q.add(pq.poll());
            }
            return q;
    }

    // make a list of unitIDs, corresponding units of these IDs are in same order with unitQueue
    public List<Integer> generateIDList(Queue<Unit> q){
        List<Unit> unitList = new ArrayList<>(q);
        List<Integer> unitIDList = new ArrayList<>();
        for(Unit u : unitList) unitIDList.add(u.getId());
        return unitIDList;
    }

    // handle "attack" message, use existing unitQueue and modify it*/
    public BattleResultMessage doBattle(BattleRequestMessage request, int playerID){
        BattleResultMessage result = new BattleResultMessage();

        int territoryID = request.getTerritoryID();
        int attackeeID = request.getAttackeeID();
        int attackerID = request.getAttackerID();
        int deletedID = -1;

        //check whether the monster exist in the territory
//        Monster monster = myMonsterManger.getMonster(attackeeID);
//        if(monster == null || monster.getTerritory().getId() != territoryID){
//            result.setResult("invalid");
//            return result;
//        }

        //begin battle
        Unit attacker = myUnitManager.getUnit(attackerID);
        Unit attackee = myUnitManager.getUnit(attackeeID);
        if(attackee == null || attacker==null){
            result.setResult("invalid");
            return result;
        }
        int attckeeHp = attackee.getHp();
        int attackerAtk = attacker.getAtk();
        int newAttackeeHp = Math.max(attckeeHp - attackerAtk, 0);
        attackee.setHp(newAttackeeHp);
        myUnitManager.setUnitHp(attackeeID, newAttackeeHp);

        if(newAttackeeHp == 0){
            result.setResult("win");
            deletedID = attackeeID;
            myUnitManager.deleteUnit(attackeeID);
        }
        else{
            result.setResult("continue");
        }

        //update unitQueue
        this.unitQueue = rollUnitQueue(this.unitQueue, deletedID);
        result.setUnitIDs(generateIDList(unitQueue));
        result.setMonsters(myMonsterManger.getMonsters(territoryID));
        result.setSoldiers(mySoldierManger.getSoldiers(playerID));
        return result;
    }

    //rolls the queue, this round's attacker will be rolled to the back of the queue, delete units that lose the battle
    public Queue<Unit> rollUnitQueue(Queue<Unit> queue, int deletedID){
        Queue<Unit> rolledQueue = new LinkedList<>();
        int firstID = queue.peek().getId();

        //copy alive unit into new rolled queue
        while(!queue.isEmpty()){
            if(queue.peek().getId() == deletedID) queue.poll();
            else rolledQueue.offer(queue.poll());
        }
        //roll rolledQueue by one element
        if(rolledQueue.peek().getId() == firstID) rolledQueue.offer(rolledQueue.poll());

        return rolledQueue;
    }
}
