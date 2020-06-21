package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.*;
import org.hibernate.Session;

import java.util.TimerTask;

public class MonsterGenerator extends TimerTask {
    public static int X_RANGE = 20;
    public static int Y_RANGE = 20;
    public static int LIMIT = 2;
    private WorldCoord[] currentCoords;
    private boolean[] canGenerateMonster;
    private MonsterManger monsterDAO;
    private Session session;

    public MonsterGenerator(WorldCoord[] coord, boolean[] canGenerateMonster) {
        this.canGenerateMonster = canGenerateMonster;
        this.currentCoords = coord;
        this.session = HibernateUtil.getSessionFactory().openSession();
        this.monsterDAO = new MonsterManger(session);
    }

    @Override
    public void run() {
        System.out.println("coord is "+ currentCoords[0]+", can is "+canGenerateMonster[0]);
        if(!canGenerateMonster[0] || this.currentCoords[0].getWid() == -1) return;

        //choose a location to generate monster
        //int tame = new TerritoryDAO(session).getTerritory(newCoord).getTame();
        session.beginTransaction();
        WorldCoord newCoord = new WorldCoord();
        newCoord.setWid(this.currentCoords[0].getWid());
        newCoord.setY(this.currentCoords[0].getY());
        newCoord.setX(this.currentCoords[0].getX()+1);

        //if number of monsters in a range in in limited number, generate a new monster
        Long monsterNum = monsterDAO.countMonsters(currentCoords[0], X_RANGE, Y_RANGE);
        System.out.println("monsterNum near currentCoord" + currentCoords[0]+ " is " + monsterNum);
        if(monsterNum <= LIMIT){
            Monster m = new Monster("wolf", 60, 6, 10);
            monsterDAO.addMonster(m, newCoord);
            System.out.println("generate a new monster in " + newCoord.toString());
        }
        session.getTransaction().commit();
    }
}
