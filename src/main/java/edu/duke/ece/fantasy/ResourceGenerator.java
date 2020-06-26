package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.DAO.PlayerDAO;
import edu.duke.ece.fantasy.database.WorldCoord;
import org.hibernate.Session;

public class ResourceGenerator extends Task {
    Session session;
    WorldCoord[] coord;

    public ResourceGenerator(long when, WorldCoord[] coord, Session session) {
        super(when, 1000, true);
        this.coord = coord;
        this.session = session;
    }

    @Override
    void doTask() {
        if(coord[0]==null) return;
        session.beginTransaction();
        PlayerDAO playerDAO = new PlayerDAO(session);
        Player player = playerDAO.getPlayerByWid(coord[0].getWid());
//        System.out.println("Generating resource, speed:" + player.getMoneyGenerationSpeed());
        if (player.getMoney() + player.getMoneyGenerationSpeed() < 9999999) {
            player.setMoney(player.getMoney() + player.getMoneyGenerationSpeed());
        }
        session.getTransaction().commit();
        session.evict(player);
    }

}
