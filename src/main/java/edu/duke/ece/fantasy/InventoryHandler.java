package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.InventoryDAO;
import edu.duke.ece.fantasy.database.DAO.PlayerDAO;
import edu.duke.ece.fantasy.database.DAO.PlayerInventoryDAO;
import edu.duke.ece.fantasy.json.AttributeRequestMessage;
import edu.duke.ece.fantasy.json.InventoryRequestMessage;
import edu.duke.ece.fantasy.json.InventoryResultMessage;
import org.hibernate.Session;

import java.util.List;

public class InventoryHandler {
    PlayerDAO playerDAO;
    InventoryDAO inventoryDAO;
    UnitManager unitManager;
    Session session;
    PlayerInventoryDAO playerInventoryDAO;

    public InventoryHandler(Session session) {
        playerDAO = new PlayerDAO(session);
        playerInventoryDAO = new PlayerInventoryDAO(session);
        inventoryDAO = new InventoryDAO(session);
        unitManager = new UnitManager(session);
        this.session = session;
    }

    public InventoryResultMessage handle(InventoryRequestMessage request, int player_id) {
        // get related object from database
        String action = request.getAction();
        InventoryResultMessage resultMessage = new InventoryResultMessage();
        Player player = playerDAO.getPlayer(player_id);
        int item_id = request.getInventoryID();
        Inventory itemPack = inventoryDAO.getInventory(item_id);
        Unit unit = unitManager.getUnit(request.getUnitID());
        try {
            if (action.equals("list")) {
                resultMessage.setResult("valid");
            } else if (action.equals("use")) {
                if (validate(player, itemPack)) {
                    player.useItem(itemPack, 1, unit);
                }
                // remove itempack from database if it is 0
                if (itemPack.getAmount() == 0) {
                    session.delete(itemPack);
                }
                resultMessage.setResult("valid");
            } else if (action.equals("drop")) {
//                if (validate(player, itemPack)) {
//                    player.dropItem(itemPack, 1);
//                }
//                // remove itempack from database if doesn't belongs to the player
//                if (itemPack.getPlayer() == null) {
//                    session.delete(itemPack);
//                }
//                session.update(player);
//                resultMessage.setResult("valid");
            }
        } catch (Exception e) {
            resultMessage.setResult("invalid:" + e.getMessage());
        }

        List<playerInventory> playerInventoryList = playerInventoryDAO.getInventories(player);
        for (playerInventory db_item : playerInventoryList) {
            // add more information of item
            Inventory toClientInventory = new Inventory(db_item.getId(), db_item.getDBItem().toGameItem().toClient(), db_item.getAmount());
            resultMessage.addItem(toClientInventory);
        }

        AttributeRequestMessage attributeRequestMessage = new AttributeRequestMessage();
        resultMessage.setAttributeResultMessage((new AttributeHandler(session)).handle(attributeRequestMessage, player_id));
        resultMessage.setMoney(player.getMoney());

        return resultMessage;
    }

    public boolean validate(Player player, Inventory itemPack) throws Exception {
        // check if player have item
        if (!player.checkItem(itemPack, 1)) {
            throw new Exception("Don't have enough item");
        }

        return true;
    }
}
