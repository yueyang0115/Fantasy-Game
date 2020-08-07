package edu.duke.ece.fantasy.task;

import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.Monster;
import edu.duke.ece.fantasy.database.WorldCoord;
import edu.duke.ece.fantasy.json.MessagesS2C;
import edu.duke.ece.fantasy.net.UserSession;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class MonsterMover extends MonsterScheduledTask {

    public MonsterMover(long when, int repeatedInterval, boolean repeating, UserSession session) {
        super(when, repeatedInterval, repeating, session);
    }


    @Override
    public void action() {
        if (cannotGenerateMonster()) return;

        //get monsters within an area whenever getting into a new coord
        List<Monster> monsterList = session.getMetaDAO().getMonsterDAO().getMonstersInRange(player.getCurrentCoord(), X_RANGE, Y_RANGE);
        //if has monsters in this area
        if (monsterList != null && monsterList.size() != 0) {
            //sort all monsters according to its distance from currentCoord
            Collections.sort(monsterList, new Comparator<Monster>() {
                @Override
                public int compare(Monster o1, Monster o2) {
//                    return o1.getId() - o2.getId();
                    double distance1 = Math.pow(o1.getCoord().getX() - player.getCurrentCoord().getX(), 2) + Math.pow(o1.getCoord().getY() - player.getCurrentCoord().getY(), 2);
                    double distance2 = Math.pow(o2.getCoord().getX() - player.getCurrentCoord().getX(), 2) + Math.pow(o2.getCoord().getY() - player.getCurrentCoord().getY(), 2);
                    return Double.compare(distance1, distance2);
                }
            });
            //choose the monster which is nearest to currentCoord and move it
            Monster movingMonster = monsterList.get(0);
            moveMonster(movingMonster);
        }
    }

    // moving monster to approach player's currentCoord
    private void moveMonster(Monster m) {
        if (m == null || m.getCoord().equals(player.getCurrentCoord())) return;
        WorldCoord startCoord = new WorldCoord(m.getCoord());
        int startX = m.getCoord().getX();
        int startY = m.getCoord().getY();
        int endX = player.getCurrentCoord().getX();
        int endY = player.getCurrentCoord().getY();

        Random rand = new Random();
        boolean moved = false;

        // move monster's x coordinate
        if (startX != endX) {
            int randomNum = rand.nextInt(9) + 0;
            if (randomNum % 2 == 0) {
                startX = startX < endX ? startX + 1 : startX - 1;
                moved = true;
            }
        }
        // move monster's y coordinate
        if (startY != endY) {
            int randomNum = rand.nextInt(9) + 0;
            if (randomNum % 2 == 0) {
                startY = startY < endY ? startY + 1 : startY - 1;
                moved = true;
            }
        }
        //update moved monster data in database,
        if (moved) {
            //update monster's new coord
            m.setCoord(new WorldCoord(m.getCoord().getWid(), startX, startY));
            session.getMetaDAO().getMonsterDAO().updateMonsterCoord(m.getId(), startX, startY);
            // set monster's needUpdate field to be true
            session.getMetaDAO().getMonsterDAO().setMonsterStatus(m.getId(), true);
            System.out.println("moving monsterID " + m.getId() + " from " + startCoord + " to " + startX + ", " + startY);
            //add the changed monster message in resultMsgQueue
            putMonsterInResultMsgQueue(m);
        }
    }

}
