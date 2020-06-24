package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.MessagesS2C;
import org.hibernate.Session;

import java.util.concurrent.LinkedBlockingQueue;

public class MonsterGenerator extends Task {
    private int MONSTER_LIMIT = 3;
    private static int TAME_LIMIT = 0;
    private MonsterManger monsterDAO;
    private TerritoryDAO territoryDAO;

    public MonsterGenerator(int when, int repeatedInterval, boolean repeating, Session session, WorldCoord[] coord, boolean[] canGenerateMonster, LinkedBlockingQueue<MessagesS2C> resultMsgQueue) {
        super(when, repeatedInterval, repeating, session, coord, canGenerateMonster, resultMsgQueue);
        monsterDAO = new MonsterManger(session);
        territoryDAO = new TerritoryDAO(session);
    }

    @Override
    public void doTask() {
        session.beginTransaction();
//        if(!canGenerateMonster[0] || this.coord[0] ==null || this.coord[0].getWid() == -1){
//            session.getTransaction().commit();
//            return;
//        }

        if(canGenerateMonster[0] && this.coord[0] != null && this.coord[0].getWid() != -1 ) {
            //if number of monsters in an area is in limited number, generate a new monster
            Long monsterNum = monsterDAO.countMonstersInRange(coord[0], X_RANGE, Y_RANGE);
            //System.out.println("monsterNum near currentCoord" + currentCoords[0]+ " is " + monsterNum);
            if (monsterNum < MONSTER_LIMIT) {
                Monster m = new Monster("wolf", 60, 6, 10);
                WorldCoord where = generateCoord(coord[0]);
                monsterDAO.addMonster(m, where);
                putMonsterInResultMsgQueue(m);
                System.out.println("generate a new monster in " + where.toString());
            }
        }
        session.getTransaction().commit();
    }

    //find a new coord to generate a new monster
    private WorldCoord generateCoord(WorldCoord currentCoord){
        WorldCoord newCorod = territoryDAO.getWildestCoordInRange(currentCoord,20,20);
        System.out.println("generate newCoord is "+newCorod);
        return newCorod;
    }
}
