package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.Monster;
import edu.duke.ece.fantasy.database.MonsterManger;
import edu.duke.ece.fantasy.database.WorldCoord;
import edu.duke.ece.fantasy.json.MessagesS2C;
import edu.duke.ece.fantasy.json.PositionResultMessage;
import org.hibernate.Session;
import org.hibernate.SharedSessionContract;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Task {
    private long when; // the time when the task should be executed
    private int repeatedInterval;
    private boolean repeating;
    private MonsterManger monsterDAO;

    protected LinkedBlockingQueue<MessagesS2C> resultMsgQueue;
    protected Session session;
    protected WorldCoord[] coord;
    protected boolean[] canGenerateMonster;

    public static int X_RANGE = 20;
    public static int Y_RANGE = 20;

    public Task(long when, int repeatedInterval, boolean repeating, Session session, WorldCoord[] coord, boolean[] canGenerateMonster, LinkedBlockingQueue<MessagesS2C> resultMsgQueue) {
        this.when = when;
        this.repeatedInterval = repeatedInterval;
        this.repeating = repeating;

        this.session = session;
        this.resultMsgQueue = resultMsgQueue;
        this.coord = coord;
        this.canGenerateMonster = canGenerateMonster;
        this.monsterDAO = new MonsterManger(session);
    }

    abstract void doTask();

    public int getRepeatedInterval() { return repeatedInterval; }

    public void setRepeatedInterval(int repeatedInterval) { this.repeatedInterval = repeatedInterval; }

    public long getWhen() { return when; }

    public void setWhen(long when) { this.when = when; }

    public void updateWhen(){ this.when += repeatedInterval; }

    public boolean isRepeating() { return repeating; }

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

}
