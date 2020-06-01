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
        BattleResultMessage result = new BattleResultMessage();
        int territoryID = request.getTerritoryID();
        int monsterID = request.getMonsterID();
        int soldierID = request.getSoldierID();
        String action = request.getAction();

        if(action.equals("escape")){
            result.setResult("escaped");
        }
        else{
            doBattle();
        }
        return result;
    }

    public void doBattle(){

    }
}
