package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Monster;
import edu.duke.ece.fantasy.database.MonsterManger;
import edu.duke.ece.fantasy.database.WorldCoord;
import org.hibernate.Session;

import javax.swing.plaf.IconUIResource;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

public class MonsterGenerator extends TimerTask {
    private WorldCoord[] coord;
    private boolean[] canGenerateMonster;
    private MonsterManger monsterDAO;
    private Session session;

    public MonsterGenerator(WorldCoord[] coord, boolean[] canGenerateMonster) {
        this.canGenerateMonster = canGenerateMonster;
        this.coord = coord;
        this.session = HibernateUtil.getSessionFactory().openSession();
        this.monsterDAO = new MonsterManger(session);
    }

    @Override
    public void run() {
        System.out.println("coord is "+coord[0]+", can is "+canGenerateMonster[0]);
        if(!canGenerateMonster[0] || this.coord[0].getWid() == -1) return;
        //TODO: yy: count num of monster in current tile, if < DEFINE_NUM, generate a new monster
        //TODO: yy: choose new coord according to coord's tame
        session.beginTransaction();
        WorldCoord newCoord = new WorldCoord();
        newCoord.setWid(this.coord[0].getWid());
        newCoord.setY(this.coord[0].getY());
        newCoord.setX(this.coord[0].getX()+1);
        List<Monster> monsters = monsterDAO.getMonsters(newCoord);
        if(monsters.size() <= 2){
            Monster m = new Monster("wolf", 60, 6, 10);
            monsterDAO.addMonster(m, newCoord);
            System.out.println("generate a new monster in " + newCoord.toString());
        }
        session.getTransaction().commit();
    }
}
