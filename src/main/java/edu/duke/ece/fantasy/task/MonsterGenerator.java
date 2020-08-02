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
            // get num of monsters in an area
            int monsterNum = MetaDAO.getMonsterDAO().countMonstersInRange(player.getCurrentCoord(), X_RANGE, Y_RANGE);
            //if the num is less than limited number, generate a new monster
            if (monsterNum < MONSTER_LIMIT) {
                Monster m = new Monster("wolf", 60, 6, 10);
                // find a coord to generate a new monster
                WorldCoord where = generateCoord(player.getCurrentCoord());
                if(where != null) {
                    // add one monster
                    m.setCoord(where);
                    MetaDAO.getMonsterDAO().addMonster(m, where);
                    //save the changed monster message in resultMsgQueue
                    putMonsterInResultMsgQueue(m);
                    System.out.println("generate a new monster in " + where.toString());
                }
            }
        }
    }

    //find a coord with the biggest tame within an area
    private WorldCoord generateCoord(WorldCoord currentCoord){
        WorldCoord newCorod = metaDAO.getTerritoryDAO().getWildestCoordInRange(currentCoord,X_RANGE,Y_RANGE);
        return newCorod;
    }
}
