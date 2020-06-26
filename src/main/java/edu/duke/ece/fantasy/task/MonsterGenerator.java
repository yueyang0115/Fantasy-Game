package edu.duke.ece.fantasy.task;

import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.TerritoryDAO;
import edu.duke.ece.fantasy.json.MessagesS2C;
import org.hibernate.Session;

import java.util.concurrent.LinkedBlockingQueue;

public class MonsterGenerator extends MonsterScheduledTask {
    private int MONSTER_LIMIT = 3;
    private static int TAME_LIMIT = 0;
    private TerritoryDAO territoryDAO;

    public MonsterGenerator(long when, int repeatedInterval, boolean repeating, MetaDAO metaDAO, Player player, LinkedBlockingQueue<MessagesS2C> resultMsgQueue) {
        super(when, repeatedInterval, repeating, metaDAO, player, resultMsgQueue);
        territoryDAO = metaDAO.getTerritoryDAO();
    }

    @Override
    public void doTask() {

        if(!canGenerateMonster()) return;

        else{
            //if number of monsters in an area is within limited number, generate a new monster
            Long monsterNum = monsterDAO.countMonstersInRange(player.getCurrentCoord(), X_RANGE, Y_RANGE);
            if (monsterNum < MONSTER_LIMIT) {
                Monster m = new Monster("wolf", 60, 6, 10);
                WorldCoord where = generateCoord(player.getCurrentCoord());
                monsterDAO.addMonster(m, where);
                //save the changed monster message in resultMsgQueue
                putMonsterInResultMsgQueue(m);
                System.out.println("generate a new monster in " + where.toString());
            }
        }
    }

    //find a new coord to generate a new monster
    private WorldCoord generateCoord(WorldCoord currentCoord){
        WorldCoord newCorod = territoryDAO.getWildestCoordInRange(currentCoord,X_RANGE,Y_RANGE);
        return newCorod;
    }
}
