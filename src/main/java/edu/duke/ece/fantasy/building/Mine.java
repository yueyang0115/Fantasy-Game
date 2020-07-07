package edu.duke.ece.fantasy.building;


import edu.duke.ece.fantasy.database.DAO.MetaDAO;
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
    public void onCreate(MetaDAO metaDAO, WorldCoord coord) {
        super.onCreate(metaDAO, coord);
        PlayerDAO playerDAO = metaDAO.getPlayerDAO();
        Player player = playerDAO.getPlayerByWid(coord.getWid());
        player.setMoneyGenerationSpeed(player.getMoneyGenerationSpeed() + MoneyGenerationSpeed);
    }
}
