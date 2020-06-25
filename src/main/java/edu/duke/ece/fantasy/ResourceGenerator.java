package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.PlayerDAO;
import edu.duke.ece.fantasy.database.WorldCoord;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import java.util.TimerTask;

public class ResourceGenerator extends Task {
    Session session;
    WorldCoord coord;

    public ResourceGenerator(long when,WorldCoord coord) {
        session = HibernateUtil.getSessionFactory().openSession();
        this.coord = coord;
        super(when,1000,true,session);
    }

    @Override
    void doTask() {
        session.beginTransaction();
        PlayerDAO playerDAO = new PlayerDAO(session);
        Player player = playerDAO.getPlayerByWid(coord.getWid());
//        System.out.println("Generating resource, speed:" + player.getMoneyGenerationSpeed());
        if (player.getMoney() + player.getMoneyGenerationSpeed() < 9999999) {
            player.setMoney(player.getMoney() + player.getMoneyGenerationSpeed());
        }
        session.getTransaction().commit();
//        session.evict(player);
    }

}
