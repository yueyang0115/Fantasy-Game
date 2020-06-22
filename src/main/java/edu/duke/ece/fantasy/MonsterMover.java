package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Monster;
import edu.duke.ece.fantasy.database.MonsterManger;
import edu.duke.ece.fantasy.database.WorldCoord;
import org.hibernate.Session;

import java.util.*;

public class MonsterMover extends TimerTask {
    volatile WorldCoord[] currentCoords;
    volatile boolean[] canGenerateMonster;
    private MonsterManger monsterDAO;
    private Session session;
    public static int X_RANGE = 20;
    public static int Y_RANGE = 20;

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
        List<Monster> monsterList = monsterDAO.getMonstersInRange(currentCoords[0],X_RANGE,Y_RANGE);
        if(monsterList == null || monsterList.size() == 0) return;

        Collections.sort(monsterList, new Comparator<Monster>(){
            @Override
            public int compare(Monster o1, Monster o2) {
                return o1.getId() - o2.getId();
            }
        });

        // choose a new moving monster whenever old one is beaten, arrived in currentCoord and get into new coord
        Monster movingMonster = null;
        for(Monster m:monsterList){
            if(!m.getCoord().equals(currentCoords[0])){
                movingMonster = m;
                break;
            }
        }

        if(movingMonster != null){
            moveMonster(movingMonster);
        }
        session.getTransaction().commit();
    }

    private void moveMonster(Monster m){
        if(m == null || m.getCoord().equals(currentCoords[0])) return;
        System.out.println("moving monsterID " + m.getId());
        WorldCoord startCoord = new WorldCoord(m.getCoord());
        int startX = m.getCoord().getX();
        int startY = m.getCoord().getY();
        int endX = currentCoords[0].getX();
        int endY = currentCoords[0].getY();

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

        if(moved) {
            monsterDAO.updateMonsterCoord(m.getId(), startX, startY);
            monsterDAO.setMonsterStatus(m.getId(), true);
            System.out.println("moving monsterID " + m.getId() +" from "+startCoord + "to "+startX+", "+startY);
        }
    }

}
