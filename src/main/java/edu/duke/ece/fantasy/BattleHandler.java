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

    public BattleResultMessage handle(BattleRequestMessage request){
        String action = request.getAction();
        BattleResultMessage result = new BattleResultMessage();
        if(action.equals("escape")){
            result.setResult("escaped");
        }
        else{
            doBattle(request, result);
        }
        return result;
    }

    private void doBattle(BattleRequestMessage request, BattleResultMessage result){
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
        //result.setSoldiers();

        //soldier attack monster, reduce monster's hp
        Soldier soldier = mySoldierManger.getSoldier(soldierID);
        int monsterHp = monster.getHp();
        int soldierAtk = soldier.getAtk();
        int newMonsterHp = Math.max(monsterHp - soldierAtk, 0);
        if(newMonsterHp == 0){
            result.setResult("win");
        }
        else{
            result.setResult("continue");
        }
        myMonsterManger.setMonsterHp(monsterID,monsterHp-soldierAtk);
    }
}
