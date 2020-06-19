package edu.duke.ece.fantasy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Monster;
import edu.duke.ece.fantasy.database.MonsterManger;
import edu.duke.ece.fantasy.json.MessagesS2C;
import edu.duke.ece.fantasy.json.PositionResultMessage;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

public class MonsterDetector extends TimerTask {
    private Session session;
    private MonsterManger monsterDAO;
    private boolean canGenerateMonster;
    private LinkedBlockingQueue<MessagesS2C> messageS2CQueue;

    public MonsterDetector(boolean canGenerateMonster, LinkedBlockingQueue<MessagesS2C> messageS2CQueue){
        this.session = HibernateUtil.getSessionFactory().openSession();
        this.monsterDAO = new MonsterManger(session);
        this.canGenerateMonster = canGenerateMonster;
        this.messageS2CQueue = messageS2CQueue;
    }

    @Override
    public void run() {
        if(!canGenerateMonster) return;
        List<Monster> monsterList = monsterDAO.getUpdatedMonsters();
        if(monsterList != null){
            monsterDAO.setMonstersStatus(monsterList, false);
            MessagesS2C result = new MessagesS2C();
            PositionResultMessage positionMsg= new PositionResultMessage();
            positionMsg.setMonsterArray(monsterList);
            result.setPositionResultMessage(positionMsg);
            messageS2CQueue.offer(result);
        }
    }
}
