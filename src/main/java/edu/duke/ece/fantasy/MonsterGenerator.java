package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.TimerTask;

public class MonsterGenerator extends TimerTask {
    public static int X_RANGE = 20;
    public static int Y_RANGE = 20;
    public static int MONSTER_LIMIT = 3;
    public static int TAME_LIMIT = 0;
    volatile WorldCoord[] currentCoords;
    volatile boolean[] canGenerateMonster;
    private MonsterManger monsterDAO;
    private TerritoryDAO territoryDAO;
    private Session session;

    public MonsterGenerator(Session session, WorldCoord[] coord, boolean[] canGenerateMonster) {
        this.canGenerateMonster = canGenerateMonster;
        this.currentCoords = coord;
        this.session = session;
        this.monsterDAO = new MonsterManger(session);
        this.territoryDAO = new TerritoryDAO(session);
    }

    @Override
    public void run() {
        if(!session.getTransaction().isActive()) session.beginTransaction();
        Transaction tx = session.getTransaction();

        //System.out.println("coord is "+ currentCoords[0]+", can is "+canGenerateMonster[0]);
        if(!canGenerateMonster[0] || this.currentCoords[0] ==null || this.currentCoords[0].getWid() == -1){
            if(tx.getStatus() != TransactionStatus.COMMITTED) tx.commit();
            return;
        }

        //if number of monsters in a range in in limited number, generate a new monster
        Long monsterNum = monsterDAO.countMonstersInRange(currentCoords[0], X_RANGE, Y_RANGE);
        //System.out.println("monsterNum near currentCoord" + currentCoords[0]+ " is " + monsterNum);
        if(monsterNum < MONSTER_LIMIT){
            Monster m = new Monster("wolf", 60, 6, 10);
            WorldCoord where = generateCoord(currentCoords[0]);
            monsterDAO.addMonster(m, where);
            System.out.println("generate a new monster in " + where.toString());
        }

        if(tx.getStatus() != TransactionStatus.COMMITTED) tx.commit();
    }

    //find a new coord to generate a new monster
    public synchronized WorldCoord generateCoord(WorldCoord currentCoord){
        //int tame = new TerritoryDAO(session).getTerritory(newCoord).getTame();
//        WorldCoord newCoord = new WorldCoord();
//        newCoord.setWid(currentCoord.getWid());
//        newCoord.setY(currentCoord.getY()+2);
//        newCoord.setX(currentCoord.getX()+2);
//        return newCoord;
        WorldCoord newCorod = territoryDAO.getWildestCoordInRange(currentCoord,20,20);
        System.out.println("generate newCoord is "+newCorod);
        return newCorod;
    }
}
