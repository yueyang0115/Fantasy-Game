package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.Item.InvalidItemUsageException;
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
    PlayerInventoryDAO playerInventoryDAO;


    public InventoryHandler() {
        playerDAO = MetaDAO.getPlayerDAO();
        playerInventoryDAO = MetaDAO.getPlayerInventoryDAO();
        inventoryDAO = MetaDAO.getInventoryDAO();
        unitDAO = MetaDAO.getUnitDAO();
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
            // convert dbInventory to clientInventory which contains more properties info
            resultMessage.addItem(eachInventory.toClient());
        }

//        resultMessage.setItems(playerInventoryList);

        resultMessage.setMoney(player.getMoney());

        return resultMessage;
    }

    private void useItem(playerInventory selectedInventory, Player player, Unit unit) throws InvalidItemUsageException {
        if (selectedInventory.getPlayer() == player) {
            selectedInventory.useItem(unit,player);
            // remove inventory from database if it is 0
            if (selectedInventory.getAmount() == 0) {
                player.getItems().remove(selectedInventory);
                HibernateUtil.delete(selectedInventory);
            }
        } else {
            throw new InvalidItemUsageException("Player doesn't have item");
        }
    }
}
