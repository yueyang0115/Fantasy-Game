package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.*;
import edu.duke.ece.fantasy.json.AttributeRequestMessage;
import edu.duke.ece.fantasy.json.InventoryRequestMessage;
import edu.duke.ece.fantasy.json.InventoryResultMessage;
import org.hibernate.Session;

import java.util.List;

public class InventoryHandler {
    PlayerDAO playerDAO;
    InventoryDAO inventoryDAO;
    UnitDAO unitDAO;
    Session session;
    PlayerInventoryDAO playerInventoryDAO;
    MetaDAO metaDAO;

    public InventoryHandler(MetaDAO metaDAO) {
        playerDAO = metaDAO.getPlayerDAO();
        playerInventoryDAO = metaDAO.getPlayerInventoryDAO();
        inventoryDAO = metaDAO.getInventoryDAO();
        unitDAO = metaDAO.getUnitDAO();
        this.metaDAO = metaDAO;
        session = metaDAO.getSession();
    }

    public InventoryResultMessage handle(InventoryRequestMessage request, int player_id) {
        // get related object from database
        String action = request.getAction();
        InventoryResultMessage resultMessage = new InventoryResultMessage();
        Player player = playerDAO.getPlayer(player_id);
        int item_id = request.getInventoryID();
        playerInventory selectedInventory = playerInventoryDAO.getInventory(item_id);
        Unit unit = unitDAO.getUnit(request.getUnitID());
        try {
            if (action.equals("list")) {
                resultMessage.setResult("valid");
            } else if (action.equals("use")) {
                useItem(selectedInventory, player, unit);
                resultMessage.setResult("valid");
            } else if (action.equals("drop")) {
//                if (validate(player, itemPack)) {
//                    player.dropItem(itemPack, 1);
//                }
//                // remove itempack from database if doesn't belongs to the player
//                if (itemPack.getPlayer() == null) {
//                    session.delete(itemPack);
//                }
//                resultMessage.setResult("valid");
            }
        } catch (Exception e) {
            resultMessage.setResult("invalid:" + e.getMessage());
        }

        List<Inventory> playerInventoryList = playerInventoryDAO.getInventories(player);
        for (Inventory eachInventory : playerInventoryList) {
            // add more information of item
            eachInventory.setDBItem(eachInventory.getDBItem().toGameItem().toClient());
        }
        resultMessage.setItems(playerInventoryList);

        AttributeRequestMessage attributeRequestMessage = new AttributeRequestMessage();
        resultMessage.setAttributeResultMessage((new AttributeHandler(metaDAO)).handle(attributeRequestMessage, player_id));
        resultMessage.setMoney(player.getMoney());

        return resultMessage;
    }

    private void useItem(playerInventory selectedInventory, Player player, Unit unit) throws Exception {
        if (selectedInventory.getPlayer() == player) {
            selectedInventory.getDBItem().toGameItem().OnUse(unit);
            selectedInventory.reduceAmount(1);
            // remove inventory from database if it is 0
            if (selectedInventory.getAmount() == 0) {
                session.delete(selectedInventory);
            }
        } else {
            throw new Exception("Player doesn't have item");
        }
    }
}
