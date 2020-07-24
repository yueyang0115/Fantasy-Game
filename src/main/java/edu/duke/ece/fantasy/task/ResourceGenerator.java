package edu.duke.ece.fantasy.task;

import edu.duke.ece.fantasy.SharedData;
import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.DAO.PlayerDAO;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.json.MessagesS2C;

import java.util.concurrent.LinkedBlockingQueue;

public class ResourceGenerator extends ScheduledTask {
//    private MetaDAO metaDAO;
    private SharedData sharedData;
//    private LinkedBlockingQueue<MessagesS2C> resultMsgQueue;

    public ResourceGenerator(long when, int repeatedInterval, boolean repeating, SharedData sharedData) {
        super(when, repeatedInterval, repeating);
//        this.metaDAO = metaDAO;
        this.sharedData = sharedData;
//        this.resultMsgQueue = resultMsgQueue;
    }

    @Override
    public void doTask() {
        Player player = sharedData.getPlayer();
//        System.out.println("Generating resource, speed:" + player.getMoneyGenerationSpeed());
        if (player.getMoney() + player.getMoneyGenerationSpeed() < 9999999) {
            player.setMoney(player.getMoney() + player.getMoneyGenerationSpeed());
        }
    }
}
