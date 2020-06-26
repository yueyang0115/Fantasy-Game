package edu.duke.ece.fantasy.task;

import edu.duke.ece.fantasy.database.Monster;
import edu.duke.ece.fantasy.database.MonsterManger;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.WorldCoord;
import edu.duke.ece.fantasy.json.MessagesS2C;
import edu.duke.ece.fantasy.json.PositionResultMessage;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class MonsterScheduledTask extends ScheduledTask {
    protected LinkedBlockingQueue<MessagesS2C> resultMsgQueue;
    protected MetaDAO metaDAO;
    protected Player player;
    protected MonsterManger monsterDAO;

    protected int X_RANGE = 10;
    protected int Y_RANGE = 10;

    public MonsterScheduledTask(long when, int repeatedInterval, boolean repeating, MetaDAO metaDAO, Player player, LinkedBlockingQueue<MessagesS2C> resultMsgQueue) {
        super(when, repeatedInterval, repeating);
        this.metaDAO = metaDAO;
        this.resultMsgQueue = resultMsgQueue;
        this.player = player;
        this.monsterDAO = metaDAO.getMonsterDAO();
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

    public boolean canGenerateMonster(){
        return player.getStatus() != Player.Status.INMAIN || player.getCurrentCoord()!= null
                || player.getCurrentCoord().getWid() == -1;
    }
}
