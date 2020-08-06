package edu.duke.ece.fantasy.task;

import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.Monster;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.WorldInfo;
import edu.duke.ece.fantasy.json.MessagesS2C;
import edu.duke.ece.fantasy.World.Message.PositionResultMessage;
import edu.duke.ece.fantasy.net.UserSession;

import java.util.ArrayList;
import java.util.List;

public abstract class MonsterScheduledTask extends ScheduledTask {

    protected Player player;
    protected UserSession session;

    protected int X_RANGE = 10;
    protected int Y_RANGE = 10;

    public MonsterScheduledTask(long when, int repeatedInterval, boolean repeating, UserSession session) {
        super(when, repeatedInterval, repeating, session.getDistributeKey());
        this.player = session.getPlayer();
        this.session = session;
    }

    // add changed monster in a new msg, add this msg to resultMsgQueue, waiting to be sent
    public void putMonsterInResultMsgQueue(Monster m) {
        // generate a new resultMsg for the changed monster
        PositionResultMessage positionMsg = new PositionResultMessage();
        List<Monster> monsterList = new ArrayList<>();
        monsterList.add(m);
        positionMsg.setMonsterArray(monsterList);
        // send the positionUpdateMsg
        session.sendMsg(positionMsg);
        //change the monster's needUpdate field to false
        MetaDAO.getMonsterDAO().setMonsterStatus(m.getId(), false);
    }

    // cannot moveMonster/generateMonster when player doesn't hold valid coord or not in mainScene
    public boolean cannotGenerateMonster() {
        return !player.getStatus().equals(WorldInfo.MainWorld)
                || player.getCurrentCoord() == null
                || MetaDAO.getTerritoryDAO().getTerritory(player.getCurrentCoord()) == null;
    }
}
