package edu.duke.ece.fantasy.Item;

import edu.duke.ece.fantasy.Item.InvalidItemUsageException;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.*;
import edu.duke.ece.fantasy.Item.Message.InventoryRequestMessage;
import edu.duke.ece.fantasy.Item.Message.InventoryResultMessage;
import edu.duke.ece.fantasy.net.UserSession;

import java.util.List;

public class InventoryHandler {
    PlayerDAO playerDAO;
    InventoryDAO inventoryDAO;
    UnitDAO unitDAO;
    PlayerInventoryDAO playerInventoryDAO;


    public InventoryHandler() {
    }

    public void handle(UserSession session, InventoryRequestMessage request) {
        playerDAO = session.getMetaDAO().getPlayerDAO();
        playerInventoryDAO = session.getMetaDAO().getPlayerInventoryDAO();
        inventoryDAO = session.getMetaDAO().getInventoryDAO();
        unitDAO = session.getMetaDAO().getUnitDAO();

        // get related object from database
        String action = request.getAction();
        InventoryResultMessage resultMessage = new InventoryResultMessage();
        Player player = session.getPlayer();
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

        session.sendMsg(resultMessage);
    }

    private void useItem(playerInventory selectedInventory, Player player, Unit unit) throws InvalidItemUsageException {
        if (selectedInventory.getPlayer() == player) {
            selectedInventory.useItem(unit, player);
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
