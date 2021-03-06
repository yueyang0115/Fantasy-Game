package edu.duke.ece.fantasy.Battle;

import edu.duke.ece.fantasy.Battle.Message.BattleAction;
import edu.duke.ece.fantasy.Battle.Message.BattleInitInfo;
import edu.duke.ece.fantasy.Battle.Message.BattleRequestMessage;
import edu.duke.ece.fantasy.Battle.Message.BattleResultMessage;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.*;
import edu.duke.ece.fantasy.database.levelUp.Skill;
import edu.duke.ece.fantasy.net.UserSession;

import java.util.*;

public class BattleHandler {
    private MonsterDAO monsterDAO;
    private SoldierDAO soldierDAO;
    private UnitDAO unitDAO;
    private PlayerDAO playerDAO;
    private TerritoryDAO territoryDAO;
    private SkillDAO skillDAO;
    private UserSession session;

    private WorldCoord currentCoord;
    public static int TAME_RANGE_X = 3;
    public static int TAME_RANGE_Y = 3;

    /* unitQueue: keep track of the unit's attack order, it is first sorted by unit's speed
    the units will take turns to attack in the order of the queue,
    once a unit finishes attack, it will be added to the back of the queue */
    private Queue<Unit> unitQueue;

    public BattleHandler() {
    }

    //return a list of battleResult because doBattle may contain results of multiple rounds
    public void handle(UserSession session, BattleRequestMessage request) {
        session.beginTransaction();
        monsterDAO = session.getMetaDAO().getMonsterDAO();
        soldierDAO = session.getMetaDAO().getSoldierDAO();
        unitDAO = session.getMetaDAO().getUnitDAO();
        territoryDAO = session.getMetaDAO().getTerritoryDAO();
        playerDAO = session.getMetaDAO().getPlayerDAO();
        skillDAO = session.getMetaDAO().getSkillDAO();
        this.session = session;

        String action = request.getAction();
        if (action.equals("escape")) {
            BattleResultMessage result = new BattleResultMessage();
            result.setResult("escaped");
            session.sendMsg(result);
        } else if (action.equals("start")) {
            doStart(session, request);
        } else {
            doBattle(session, request);
        }
        session.commitTransaction();
    }

    // handle "start battle" message, generate a new unitQueue to keep track of the attacker order
    public BattleResultMessage doStart(UserSession session, BattleRequestMessage request) {

        int playerID = session.getPlayer().getId();

        // create a new unitQueue each time a new battle starts
        this.unitQueue = new LinkedList<>();
        BattleResultMessage result = new BattleResultMessage();

        MonsterDAO monsterDAO = session.getMetaDAO().getMonsterDAO();
        SoldierDAO soldierDAO = session.getMetaDAO().getSoldierDAO();
        PlayerDAO playerDAO = session.getMetaDAO().getPlayerDAO();
        // get monsters and soldiers engaged in the battle
        currentCoord = request.getTerritoryCoord();
        currentCoord.setWid(session.getPlayer().getCurWorldId());
        List<Monster> monsterList = monsterDAO.getMonsters(currentCoord);
        List<Soldier> soldierList = soldierDAO.getSoldiers(playerID);
        // sort unit by speed and set the UnitQueue
        this.unitQueue = generateUnitQueue(monsterList, soldierList);
        // make a list of unitIDs, corresponding units of these IDs are in same order with unitQueue
        List<Integer> unitIDList = generateIDList(unitQueue);

        // store battleQueue in player database
        playerDAO.setBattleInfo(playerID, unitIDList);

        result.setBattleInitInfo(new BattleInitInfo(monsterList, soldierList, unitIDList));
        result.setResult("continue");
        session.sendMsg(result);
        return result;
    }

    //sort units by speed and set the UnitQueue
    public Queue<Unit> generateUnitQueue(List<Monster> monsterList, List<Soldier> soldierList) {
        PriorityQueue<Unit> pq = new PriorityQueue(new Comparator<Unit>() {
            public int compare(Unit u1, Unit u2) {
                return u2.getSpeed() - u1.getSpeed();
            }
        });
        for (Monster monster : monsterList) pq.add(monster);
        for (Soldier soldier : soldierList) pq.add(soldier);
        Queue<Unit> q = new LinkedList<>();
        while (!pq.isEmpty()) {
            q.add(pq.poll());
        }
        return q;
    }

    // make a list of unitIDs, corresponding units of these IDs are in same order with unitQueue
    public List<Integer> generateIDList(Queue<Unit> q) {
        List<Unit> unitList = new ArrayList<>(q);
        List<Integer> unitIDList = new ArrayList<>();
        for (Unit u : unitList) unitIDList.add(u.getId());
        return unitIDList;
    }

    // handle "attack" message, use existing unitQueue and modify it
    public BattleResultMessage doBattle(UserSession session, BattleRequestMessage request) {
        int playerID = session.getPlayer().getId();

        // load battleQueue from player in database
        List<Integer> loadedUnitList = playerDAO.getBattleInfo(playerID);
        this.unitQueue = new LinkedList<>();
        for (int id : loadedUnitList) this.unitQueue.offer(unitDAO.getUnit(id));

        BattleResultMessage result = new BattleResultMessage();
        List<BattleAction> actions = new ArrayList<>();

        //player request doesn't contain wid
        WorldCoord where = request.getTerritoryCoord();
        where.setWid(session.getPlayer().getCurWorldId());

        int attackeeID = request.getBattleAction().getAttackee().getId();
        int attackerID = request.getBattleAction().getAttacker().getId();
        Skill attackerSkill = skillDAO.getSkill(request.getBattleAction().getActionType());

        BattleAction action = doBattleOnce(attackerSkill, attackerID, attackeeID, where, playerID, result);
        actions.add(action);
        setStatus(where, playerID, result);

        //if the next attacker in UnitQueue is monster, server do another monster attack
        while (this.unitQueue.peek() instanceof Monster && result.getResult().equals("continue")) {
            attackerID = this.unitQueue.peek().getId();
            attackeeID = request.getBattleAction().getAttacker().getId();
            //since right now we make monster attack the same soldier, if that soldier die, break loop
            if (unitDAO.getUnit(attackeeID) == null) break;
            if (unitDAO.getUnit(attackerID) == null) continue;
            action = doBattleOnce(null, attackerID, attackeeID, where, playerID, result);
            actions.add(action);
            setStatus(where, playerID, result);
            if (result.getResult().equals("lose")) break;
        }

        result.setActions(actions);
        session.sendMsg(result);
        return result;
    }

    //set "win" "lose" "continue" status for BattleResultMsg
    public void setStatus(WorldCoord where, int playerID, BattleResultMessage result) {
        List<Monster> monsterList = monsterDAO.getMonsters(where);
//        List<Soldier> soldierList = soldierDAO.getSoldiers(playerID);
        if (monsterList == null || monsterList.size() == 0) {
            result.setResult("win");
            //change around area's tame
            territoryDAO.updateTameByRange(where, TAME_RANGE_X, TAME_RANGE_Y, 10, 5);
        } else if (!playerIsAlive(playerID)) {
            result.setResult("lose");
            session.getPlayer().setStatus(WorldInfo.DeathWorld);
//            WorldInfo info = worldDAO.initWorld(where, playerDAO.getPlayer(playerID).getUsername(), 20);
//            info.setWorldType(WorldInfo.DeathWorld);
//            playerDAO.addWorld(playerID, info);
        } else result.setResult("continue");
    }

    public boolean playerIsAlive(int playerID) {
        List<Soldier> soldierList = soldierDAO.getSoldiers(playerID);
        boolean isAlive = false;
        for (Soldier soldier : soldierList) if (soldier.getHp() != 0) isAlive = true;
        return isAlive;
    }

    public BattleAction doBattleOnce(Skill attackerSkill, int attackerID, int attackeeID, WorldCoord where, int playerID, BattleResultMessage result) {
        BattleAction action = new BattleAction();
        int deletedID = -1;

        //begin battle
        Unit attacker = unitDAO.getUnit(attackerID);
        Unit attackee = unitDAO.getUnit(attackeeID);
        if (attackee == null || attacker == null) {
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
        if (newAttackeeHp == 0) {
            deletedID = attackeeID;
            // if soldier successfully attacks, change its level and skillPoint
            if (attacker instanceof Soldier)
                unitDAO.updateExperience(attackerID, attacker.getExperience().getExperience() + 2);
            // if soldier died, remove it from player's soldierList then delete it in db
            //if(attackee instanceof Soldier) playerDAO.removeSoldier(playerID,attackeeID);
            // delete died monster
            if (attackee instanceof Monster) unitDAO.deleteUnit(attackeeID);
        }

        //update unitQueue
        this.unitQueue = rollUnitQueue(this.unitQueue, deletedID);

        // update battleQueue in player database
        playerDAO.setBattleInfo(playerID, generateIDList(this.unitQueue));

        action.setAttackee(new Unit(attackee));
        action.setAttacker(new Unit(unitDAO.getUnit(attackerID))); // get up-to-date attacker from db
        String actionType = (attackerSkill == null) ? "normal" : attackerSkill.getName();
        action.setActionType(actionType);
        action.setUnits(generateIDList(unitQueue));
        return action;
    }

    //rolls the queue, this round's attacker will be rolled to the back of the queue, delete units that lose the battle
    public Queue<Unit> rollUnitQueue(Queue<Unit> queue, int deletedID) {
        Queue<Unit> rolledQueue = new LinkedList<>(queue);
        int firstID = queue.peek().getId();

        //copy alive unit into new rolled queue
//        while(!queue.isEmpty()){
//            if(queue.peek().getId() == deletedID) queue.poll();
//            else rolledQueue.offer(queue.poll());
//            rolledQueue.offer(queue.poll());
//        }
        //roll rolledQueue by one element
//        if(rolledQueue.peek().getId() == firstID) rolledQueue.offer(rolledQueue.poll());
        rolledQueue.offer(rolledQueue.poll());

        //jump unit with hp 0
        while (rolledQueue.peek().getId() == deletedID || rolledQueue.peek().getHp() == 0)
            rolledQueue.offer(rolledQueue.poll());

        return rolledQueue;
    }
}
