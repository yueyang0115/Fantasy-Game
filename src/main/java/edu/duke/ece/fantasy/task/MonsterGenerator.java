package edu.duke.ece.fantasy.task;

import edu.duke.ece.fantasy.SharedData;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.json.MessagesS2C;

import java.util.concurrent.LinkedBlockingQueue;

public class MonsterGenerator extends MonsterScheduledTask {
    private int MONSTER_LIMIT = 3;
    private static int TAME_LIMIT = 0;

    public MonsterGenerator(long when, int repeatedInterval, boolean repeating, MetaDAO metaDAO, SharedData sharedData, LinkedBlockingQueue<MessagesS2C> resultMsgQueue) {
        super(when, repeatedInterval, repeating, metaDAO, sharedData, resultMsgQueue);
    }

    @Override
    public void doTask() {

        if(cannotGenerateMonster()) return;

        else{
            //if number of monsters in an area is within limited number, generate a new monster
            Long monsterNum = metaDAO.getMonsterDAO().countMonstersInRange(player.getCurrentCoord(), X_RANGE, Y_RANGE);
            if (monsterNum < MONSTER_LIMIT) {
                Monster m = new Monster("wolf", 60, 6, 10);
                WorldCoord where = generateCoord(player.getCurrentCoord());
                if(where != null) {
                    metaDAO.getMonsterDAO().addMonster(m, where);
                    //save the changed monster message in resultMsgQueue
                    putMonsterInResultMsgQueue(m);
                    System.out.println("generate a new monster in " + where.toString());
                }
            }
        }
    }

    //find a new coord to generate a new monster
    private WorldCoord generateCoord(WorldCoord currentCoord){
        WorldCoord newCorod = metaDAO.getTerritoryDAO().getWildestCoordInRange(currentCoord,X_RANGE,Y_RANGE);
        return newCorod;
    }
}
