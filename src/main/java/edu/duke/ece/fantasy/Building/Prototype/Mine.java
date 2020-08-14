package edu.duke.ece.fantasy.Building.Prototype;


import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.DAO.PlayerDAO;
import edu.duke.ece.fantasy.database.WorldCoord;

public class Mine extends Building {
    int MoneyGenerationSpeed;

    public Mine() {
        super("mine", 100);
        MoneyGenerationSpeed = 10;
    }

    @Override
    public void onCreate(WorldCoord coord, MetaDAO metaDAO) {
        super.onCreate(coord, metaDAO);
        PlayerDAO playerDAO = metaDAO.getPlayerDAO();
        Player player = playerDAO.getPlayerByWid(coord.getWid());
        player.setMoneyGenerationSpeed(player.getMoneyGenerationSpeed() + MoneyGenerationSpeed);
    }
}
