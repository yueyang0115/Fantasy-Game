package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Monster;
import edu.duke.ece.fantasy.database.MonsterManger;
import edu.duke.ece.fantasy.database.WorldCoord;
import edu.duke.ece.fantasy.json.MessagesS2C;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class MonsterMover extends Task {

    private MonsterManger monsterDAO;

    public MonsterMover(long when, int repeatedInterval, boolean repeating, Session session, WorldCoord[] coord, boolean[] canGenerateMonster, LinkedBlockingQueue<MessagesS2C> resultMsgQueue) {
        super(when, repeatedInterval, repeating, session, coord, canGenerateMonster, resultMsgQueue);
        this.monsterDAO = new MonsterManger(session);
    }


    @Override
    public void doTask() {
        if(!canGenerateMonster[0] || this.coord[0] ==null || this.coord[0].getWid() == -1) return;
        session.beginTransaction();

        //get monsters within an area whenever getting into a new coord
        List<Monster> monsterList = monsterDAO.getMonstersInRange(coord[0],X_RANGE,Y_RANGE);
//        if(monsterList == null || monsterList.size() == 0){
//            if(tx.getStatus() != TransactionStatus.COMMITTED) tx.commit();
//            return;
//        }

        if(monsterList != null && monsterList.size() != 0){
            Collections.sort(monsterList, new Comparator<Monster>(){
                @Override
                public int compare(Monster o1, Monster o2) {
                    return o1.getId() - o2.getId();
                }
            });
            Monster movingMonster = monsterList.get(0);
            moveMonster(movingMonster);
        }

        session.getTransaction().commit();
    }

    private void moveMonster(Monster m){
        if(m == null || m.getCoord().equals(coord[0])) return;
        WorldCoord startCoord = new WorldCoord(m.getCoord());
        int startX = m.getCoord().getX();
        int startY = m.getCoord().getY();
        int endX = coord[0].getX();
        int endY = coord[0].getY();

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
            System.out.println("moving monsterID " + m.getId() +" from "+startCoord + " to "+startX+", "+startY);
            putMonsterInResultMsgQueue(m);
        }
    }

}
