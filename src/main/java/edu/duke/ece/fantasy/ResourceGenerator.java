package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.PlayerDAO;
import edu.duke.ece.fantasy.database.WorldCoord;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import java.util.TimerTask;

public class ResourceGenerator extends TimerTask {
    Session session;
    WorldCoord coord;

    public ResourceGenerator(WorldCoord coord) {
        session = HibernateUtil.getSessionFactory().openSession();
        this.coord = coord;
    }

    @Override
    public void run() {
        PlayerDAO playerDAO = new PlayerDAO(session);
        Player player = playerDAO.getPlayerByWid(coord.getWid());
        if (player.getMoney() + player.getMoneyGenerationSpeed() < 9999999) {
            player.setMoney(player.getMoney() + player.getMoneyGenerationSpeed());
        }
    }

}
