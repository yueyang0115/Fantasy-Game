package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.*;
import org.hibernate.Session;

public class BattleHandler {
    private Session session;
    private MonsterManger myMonsterManger;
    private SoldierManger mySoldierManger;

    public BattleHandler(Session session) {
        this.session = session;
        myMonsterManger = new MonsterManger(session);
        mySoldierManger = new SoldierManger(session);
    }

    public BattleResultMessage handle(BattleRequestMessage request, int playerID){
        String action = request.getAction();
        BattleResultMessage result = new BattleResultMessage();
        if(action.equals("escape")){
            result.setResult("escaped");
        }
        else if(action.equals("start")){
            int territoryID = request.getTerritoryID();
            result.setMonsters(myMonsterManger.getMonsters(territoryID));
            result.setSoldiers(mySoldierManger.getSoldiers(playerID));
            result.setResult("continue");
        }
        else{
            doBattle(request, playerID, result);
        }
        return result;
    }

    private void doBattle(BattleRequestMessage request, int playerID, BattleResultMessage result){
        int territoryID = request.getTerritoryID();
        int monsterID = request.getMonsterID();
        int soldierID = request.getSoldierID();

        //check the monster exist in the territory
        Monster monster = myMonsterManger.getMonster(monsterID);
        if(monster.getTerritory().getId() != territoryID){
            result.setResult("invalid");
            return;
        }

        //set monsterList and soldierList in result
        result.setMonsters(myMonsterManger.getMonsters(territoryID));
        result.setSoldiers(mySoldierManger.getSoldiers(playerID));
        //result.setSoldiers();

        //soldier attack monster, reduce monster's hp
        Soldier soldier = mySoldierManger.getSoldier(soldierID);
        int newMonsterHp = Math.max(monster.getHp() - soldier.getAtk(), 0);
        myMonsterManger.setMonsterHp(monsterID,newMonsterHp);

        if(newMonsterHp == 0){
            result.setResult("win");
        }
        else{
            result.setResult("continue");
        }
    }
}
