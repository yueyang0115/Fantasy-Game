package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.PlayerDAO;
import edu.duke.ece.fantasy.json.InventoryRequestMessage;
import edu.duke.ece.fantasy.json.InventoryResultMessage;
import org.hibernate.Session;

public class InventoryHandler {
    PlayerDAO playerDAO;

    public InventoryHandler(Session session) {
        playerDAO = new PlayerDAO(session);
    }

    public InventoryResultMessage handle(InventoryRequestMessage request, int player_id) {
        String action = request.getAction();
        InventoryResultMessage resultMessage = new InventoryResultMessage();
        Player player = playerDAO.getPlayer(player_id);
        if (action.equals("list")) {
            resultMessage.setResult("valid");
        }

        resultMessage.setItems(player.getItems());
        resultMessage.setMoney(player.getMoney());

        return resultMessage;
    }
}
