package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.Monster;
import edu.duke.ece.fantasy.database.MonsterManger;
import edu.duke.ece.fantasy.database.WorldCoord;
import edu.duke.ece.fantasy.json.MessagesS2C;
import edu.duke.ece.fantasy.json.PositionResultMessage;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class MonsterTask extends Task{
    protected LinkedBlockingQueue<MessagesS2C> resultMsgQueue;
    protected Session session;
    protected WorldCoord[] coord;
    protected boolean[] canGenerateMonster;
    protected MonsterManger monsterDAO;

    protected int X_RANGE = 10;
    protected int Y_RANGE = 10;

    public MonsterTask(long when, int repeatedInterval, boolean repeating, Session session, WorldCoord[] coord, boolean[] canGenerateMonster, LinkedBlockingQueue<MessagesS2C> resultMsgQueue) {
        super(when, repeatedInterval, repeating);
        this.session = session;
        this.resultMsgQueue = resultMsgQueue;
        this.coord = coord;
        this.canGenerateMonster = canGenerateMonster;
        this.monsterDAO = new MonsterManger(session);
    }

    public void putMonsterInResultMsgQueue(Monster m){
        // generate a new resultMsg for the changed monster, add the new Msg into resultMsgQueue
        MessagesS2C result = new MessagesS2C();
        PositionResultMessage positionMsg= new PositionResultMessage();
        List<Monster> monsterList = new ArrayList<>();
        monsterList.add(m);
        positionMsg.setMonsterArray(monsterList);
        result.setPositionResultMessage(positionMsg);
        resultMsgQueue.offer(result);
        //change the monster's needUpdate field
        monsterDAO.setMonsterStatus(m.getId(), false);
    }

    @Override
    void doTask() {
    }
}
