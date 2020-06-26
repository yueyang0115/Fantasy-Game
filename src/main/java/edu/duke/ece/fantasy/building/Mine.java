package edu.duke.ece.fantasy.building;


import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.DAO.PlayerDAO;
import edu.duke.ece.fantasy.database.WorldCoord;
import org.hibernate.Session;

public class Mine extends Building {
    int MoneyGenerationSpeed;

    public Mine() {
        super("mine", 100);
        MoneyGenerationSpeed = 10;
    }

    @Override
    public void onCreate(Session session, WorldCoord coord) {
        super.onCreate(session, coord);
        PlayerDAO playerDAO = new PlayerDAO(session);
        Player player = playerDAO.getPlayerByWid(coord.getWid());
        player.setMoneyGenerationSpeed(player.getMoneyGenerationSpeed() + MoneyGenerationSpeed);
    }
}
