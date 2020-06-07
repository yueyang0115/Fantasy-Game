package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.PlayerDAO;
import edu.duke.ece.fantasy.json.InventoryResultMessage;
import org.hibernate.Session;

public class InventoryHandler {
    PlayerDAO playerDAO;

    public InventoryHandler(Session session) {
        playerDAO = new PlayerDAO(session);
    }

    public InventoryResultMessage handle(int player_id){
        InventoryResultMessage resultMessage = new InventoryResultMessage();
        Player player = playerDAO.getPlayer(player_id);
        resultMessage.setItems(player.getItems());
        return resultMessage;
    }
}
