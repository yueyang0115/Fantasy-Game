package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.Monster;
import edu.duke.ece.fantasy.database.DAO.MonsterDAO;
import edu.duke.ece.fantasy.json.MessagesS2C;
import edu.duke.ece.fantasy.json.PositionResultMessage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

public class MonsterDetector extends TimerTask {
    private Session session;
    private MonsterDAO monsterDAO;
    private boolean[] canGenerateMonster;
    private LinkedBlockingQueue<MessagesS2C> messageS2CQueue;

    public MonsterDetector(Session session, boolean[] canGenerateMonster, LinkedBlockingQueue<MessagesS2C> messageS2CQueue){
        this.session = session;
        this.monsterDAO = new MonsterDAO(session);
        this.canGenerateMonster = canGenerateMonster;
        this.messageS2CQueue = messageS2CQueue;
    }

    @Override
    public void run() {
        if(!session.getTransaction().isActive()) session.beginTransaction();
        Transaction tx = session.getTransaction();

        if(!canGenerateMonster[0]){
            if(tx.getStatus() != TransactionStatus.COMMITTED) tx.commit();
            return;
        }

        List<Monster> monsterList = monsterDAO.getUpdatedMonsters();
        if(monsterList != null && monsterList.size() != 0){
            monsterDAO.setMonstersStatus(monsterList, false);
            MessagesS2C result = new MessagesS2C();
            PositionResultMessage positionMsg= new PositionResultMessage();
            positionMsg.setMonsterArray(monsterList);
            result.setPositionResultMessage(positionMsg);
            messageS2CQueue.offer(result);
        }

//        for(Monster monster: monsterList){
//            session.evict(monster);
//        }
        if(tx.getStatus() != TransactionStatus.COMMITTED) tx.commit();
    }
}
