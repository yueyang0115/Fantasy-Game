package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Monster;
import edu.duke.ece.fantasy.database.MonsterManger;
import edu.duke.ece.fantasy.database.WorldCoord;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

public class MonsterMover extends TimerTask {
    volatile WorldCoord[] currentCoords;
    volatile boolean[] canGenerateMonster;
    private MonsterManger monsterDAO;
    private Session session;
    public static int X_RANGE = 20;
    public static int Y_RANGE = 20;
    private List<Monster> monsterList = new ArrayList<>();
    private WorldCoord prevCoord = new WorldCoord();
    private Monster movingMonster = null;

    public MonsterMover(WorldCoord[] coord, boolean[] canGenerateMonster){
        this.canGenerateMonster = canGenerateMonster;
        this.currentCoords = coord;
        this.session = HibernateUtil.getSessionFactory().openSession();
        this.monsterDAO = new MonsterManger(session);
    }

    @Override
    public void run() {
        if(!canGenerateMonster[0] || this.currentCoords[0] ==null || this.currentCoords[0].getWid() == -1) return;
        session.beginTransaction();

        //get monsters within an area whenever getting into a new coord
        if(!prevCoord.equals(currentCoords[0])){
            this.monsterList = monsterDAO.getMonstersInRange(currentCoords[0],X_RANGE,Y_RANGE);
            this.prevCoord = new WorldCoord(currentCoords[0]);
        }

        //if no monsters, return
        if(monsterList == null || monsterList.size() == 0) return;

        // choose a new moving monster whenever old one is beaten, arrived in currentCoord and get into new coord
        if(movingMonster == null || movingMonster.getCoord() == currentCoords[0] || !prevCoord.equals(currentCoords[0])){
            for(Monster m:monsterList){
                if(m.getCoord() != currentCoords[0]){
                    movingMonster = m;
                    System.out.println("get new moving monsterID" + m.getId());
                    break;
                }
            }
        }

        if( !movingMonster.getCoord().equals(currentCoords[0])) moveMonster(movingMonster);
        session.getTransaction().commit();
    }

    private void moveMonster(Monster m){
        if(m == null) return;
        WorldCoord startCoord = m.getCoord();
        WorldCoord endCoord = currentCoords[0];
        int startX = startCoord.getX();
        int startY = startCoord.getY();
        int endX = endCoord.getX();
        int endY = endCoord.getY();

        Random rand = new Random();
        int randomNum = rand.nextInt(9) + 0;
        boolean moved = false;

        if(startX != endX){
            if(randomNum %2 == 0){
                startX = startX < endX? startX+1 : startX-1;
                moved = true;
            }
        }
        if(startY != endY){
            if(randomNum %2 == 1){
                startY = startY < endY? startY+1 : startY-1;
                moved = true;
            }
        }
        m.getCoord().setX(startX);
        m.getCoord().setY(startY);
        monsterDAO.updateMonsterCoord(m, startX, startY);
        monsterDAO.setMonsterStatus(m.getId(),true);
        if(m.getCoord().equals(currentCoords[0])){
                monsterList.remove(m);
        }
        if(moved) System.out.println("moving monsterID " + m.getId() +" from "+startCoord + "to "+startX+", "+startY);
    }

}
