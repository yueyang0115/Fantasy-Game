package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.*;
import edu.duke.ece.fantasy.database.levelUp.Skill;
import edu.duke.ece.fantasy.json.*;

import java.util.*;

public class BattleHandler {
    private MonsterDAO monsterDAO;
    private SoldierDAO soldierDAO;
    private UnitDAO unitDAO;
    private PlayerDAO playerDAO;
    private TerritoryDAO territoryDAO;
    private SkillDAO skillDAO;
    public static int TAME_RANGE_X = 3;
    public static int TAME_RANGE_Y = 3;

    /* unitQueue: keep track of the unit's attack order, it is first sorted by unit's speed
    the units will take turns to attack in the order of the queue,
    once a unit finishes attack, it will be added to the back of the queue */
    private Queue<Unit> unitQueue;

    public BattleHandler() {
    }

    //return a list of battleResult because doBattle may contain results of multiple rounds
    public BattleResultMessage handle(BattleRequestMessage request, int playerID, MetaDAO metaDAO){
        monsterDAO = metaDAO.getMonsterDAO();
        soldierDAO = metaDAO.getSoldierDAO();
        unitDAO = metaDAO.getUnitDAO();
        territoryDAO = metaDAO.getTerritoryDAO();
        playerDAO = metaDAO.getPlayerDAO();
        skillDAO = metaDAO.getSkillDAO();

        String action = request.getAction();
        if(action.equals("escape")){
            BattleResultMessage result = new BattleResultMessage();
            result.setResult("escaped");
            return result;
        }
        else if(action.equals("start")){
            return doStart(request,playerID);
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
        WorldCoord where = request.getTerritoryCoord();
        List<Monster> monsterList = monsterDAO.getMonsters(where);
        List<Soldier> soldierList = soldierDAO.getSoldiers(playerID);
        // sort unit by speed and set the UnitQueue
        this.unitQueue = generateUnitQueue(monsterList, soldierList);
        // make a list of unitIDs, corresponding units of these IDs are in same order with unitQueue
        List<Integer> unitIDList = generateIDList(unitQueue);

        result.setBattleInitInfo(new BattleInitInfo(monsterList,soldierList,unitIDList));
        result.setResult("continue");
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

    // handle "attack" message, use existing unitQueue and modify it
    public BattleResultMessage doBattle(BattleRequestMessage request, int playerID) {
        BattleResultMessage result = new BattleResultMessage();
        List<BattleAction> actions = new ArrayList<>();
        WorldCoord where = request.getTerritoryCoord();
        int attackeeID = request.getBattleAction().getAttackee().getId();
        int attackerID = request.getBattleAction().getAttacker().getId();
        Skill attackerSkill = skillDAO.getSkill(request.getBattleAction().getActionType());

        BattleAction action = doBattleOnce(attackerSkill, attackerID,attackeeID,where,playerID,result);
        actions.add(action);
        setStatus(where,playerID,result);

        //if the next attacker in UnitQueue is monster, server do another monster attack
        while(this.unitQueue.peek() instanceof Monster && result.getResult().equals("continue")){
            attackerID = this.unitQueue.peek().getId();
            attackeeID = request.getBattleAction().getAttacker().getId();
            //since right now we make monster attack the same soldier, if that soldier die, break loop
            if(unitDAO.getUnit(attackeeID) == null) break;
            if(unitDAO.getUnit(attackerID) == null) continue;
            action = doBattleOnce(null, attackerID,attackeeID,where,playerID,result);
            actions.add(action);
            setStatus(where,playerID,result);
        }

        result.setActions(actions);
        return result;
    }

    //set "win" "lose" "continue" status for BattleResultMsg
    public void setStatus(WorldCoord where, int playerID, BattleResultMessage result) {
        List<Monster> monsterList = monsterDAO.getMonsters(where);
        List<Soldier> soldierList = soldierDAO.getSoldiers(playerID);
        if(monsterList == null || monsterList.size() ==0){
            result.setResult("win");
            //change around area's tame
            territoryDAO.updateTameByRange(where,TAME_RANGE_X,TAME_RANGE_Y,10,5);
        }
        else if(soldierList == null || soldierList.size() ==0) result.setResult("lose");
        else result.setResult("continue");
    }

    public BattleAction doBattleOnce(Skill attackerSkill, int attackerID, int attackeeID, WorldCoord where, int playerID,BattleResultMessage result){
        BattleAction action = new BattleAction();
        int deletedID = -1;

        //begin battle
        Unit attacker = unitDAO.getUnit(attackerID);
        Unit attackee = unitDAO.getUnit(attackeeID);
        if(attackee == null || attacker==null){
            result.setResult("invalid");
            return null;
        }

        int attackerSkillAtk = (attackerSkill == null) ? 0 : attackerSkill.getAttack();
        int attckeeHp = attackee.getHp();
        int attackerAtk = attacker.getAtk();
        int newAttackeeHp = Math.max(attckeeHp - attackerAtk - attackerSkillAtk, 0);
        attackee.setHp(newAttackeeHp);
        unitDAO.setUnitHp(attackeeID, newAttackeeHp);

        // attackee's hp = 0, delete it
        if(newAttackeeHp == 0){
            deletedID = attackeeID;
            // if soldier successfully attacks, change its level and skillPoint
            if(attacker instanceof Soldier) unitDAO.updateExperience(attackerID, attacker.getExperience().getExperience()+2);
            // if soldier died, remove it from player's soldierList then delete it in db
            if(attackee instanceof Soldier) playerDAO.removeSoldier(playerID,attackeeID);
            unitDAO.deleteUnit(attackeeID);
        }

        //update unitQueue
        this.unitQueue = rollUnitQueue(this.unitQueue, deletedID);
        action.setAttackee(new Unit(attackee));
        action.setAttacker(new Unit(unitDAO.getUnit(attackerID))); // get up-to-date attacker from db
        action.setActionType("normal");
        action.setUnits(generateIDList(unitQueue));
        return action;
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
