package edu.duke.ece.fantasy.task;

import edu.duke.ece.fantasy.SharedData;
import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.Monster;
import edu.duke.ece.fantasy.database.DAO.MonsterDAO;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.json.MessagesS2C;
import edu.duke.ece.fantasy.json.PositionResultMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class MonsterScheduledTask extends ScheduledTask {
    protected LinkedBlockingQueue<MessagesS2C> resultMsgQueue;
    protected MetaDAO metaDAO;
    protected SharedData sharedData;
    protected Player player;

    protected int X_RANGE = 10;
    protected int Y_RANGE = 10;

    public MonsterScheduledTask(long when, int repeatedInterval, boolean repeating, MetaDAO metaDAO, SharedData sharedData, LinkedBlockingQueue<MessagesS2C> resultMsgQueue) {
        super(when, repeatedInterval, repeating);
        this.metaDAO = metaDAO;
        this.resultMsgQueue = resultMsgQueue;
        this.sharedData = sharedData;
        this.player = sharedData.getPlayer();
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
        metaDAO.getMonsterDAO().setMonsterStatus(m.getId(), false);
    }

    public boolean cannotGenerateMonster(){
        return player.getStatus() != Player.Status.INMAIN
                || player.getCurrentCoord()!= null
                || player.getCurrentCoord().getWid() == -1;
    }
}
