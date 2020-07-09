package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.Item.Item;
import edu.duke.ece.fantasy.building.Shop;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.*;
import edu.duke.ece.fantasy.json.InventoryRequestMessage;
import edu.duke.ece.fantasy.json.ShopRequestMessage;
import edu.duke.ece.fantasy.json.ShopResultMessage;
import org.hibernate.Session;

import java.util.List;
import java.util.Map;

public class ShopHandler {
    private PlayerDAO playerDAO;
    private DBBuildingDAO dbBuildingDAO;
    private InventoryDAO inventoryDAO;
    private Session session;
    private MetaDAO metaDAO;
    private PlayerInventoryDAO playerInventoryDAO;
    private ShopInventoryDAO shopInventoryDAO;

    public ShopHandler(MetaDAO metaDAO) {
        this.metaDAO = metaDAO;
        dbBuildingDAO = metaDAO.getDbBuildingDAO();
        playerDAO = metaDAO.getPlayerDAO();
        playerInventoryDAO = metaDAO.getPlayerInventoryDAO();
        shopInventoryDAO = metaDAO.getShopInventoryDAO();
        inventoryDAO = metaDAO.getInventoryDAO();
        this.session = metaDAO.getSession();
    }

    public ShopResultMessage handle(ShopRequestMessage request, int playerID) {
        Player player = playerDAO.getPlayer(playerID);
        String action = request.getAction();
//        DBShop DBShop = DBShopDAO.getShop(request.getShopID());

        Shop shop = (Shop) dbBuildingDAO.getBuilding(request.getCoord()).toGameBuilding();
        List<Inventory> selectedInventory = request.getSelectedItems();
        List<Inventory> playerInventory = playerInventoryDAO.getInventories(player);
        List<Inventory> shopInventory = shopInventoryDAO.getInventories(request.getCoord());

        ShopResultMessage result = new ShopResultMessage();

        try {
            if (action.equals("list")) {
                result.setResult("valid");//TODO:return
            } else if (action.equals("buy")) {
                validate(shopInventory, selectedInventory, player);
                execute(shopInventory, playerInventory, selectedInventory, player, shop);
                result.setResult("valid");
            } else if (action.equals("sell")) {
                validate(playerInventory, selectedInventory, player);
                execute(playerInventory, shopInventory, selectedInventory, shop, player);
                result.setResult("valid");
            }
        } catch (InvalidShopRequest e) {
//            session.getTransaction().rollback();
            result.setResult("invalid:" + e.getMessage());
        }
        // get latest data from db(previous transaction may roll back)
        for (Inventory inventory : shopInventory) {
            // convert dbInventory to clientInventory which contains more properties info
            result.addItem(inventory.toClient());
        }
//        result.setItems(shopInventory);
        return result;
//        return null;
    }


    private Inventory findInventoryFromList(List<Inventory> InventoryList, Inventory selectedInventory) {
        Inventory pairedInventory = null;
        for (Inventory inventory : InventoryList) {
            if (inventory.equals(selectedInventory)) { // check if inventoryList have the same item as selectedInventory
                pairedInventory = inventory;
                break;
            }
        }
        return pairedInventory;
    }

    private void validate(List<Inventory> sellerInventoryList, List<Inventory> selectedInventoryList, Trader buyer) throws InvalidShopRequest {
        int required_money = 0;

        for (Inventory selectedInventory : selectedInventoryList) {
            int selectedAmount = selectedInventory.getAmount();
            Item selectedItem = selectedInventory.getDBItem().toGameItem();
            Inventory pairedInventory = findInventoryFromList(sellerInventoryList, selectedInventory);//TODO: may use hashMap instead
            if (pairedInventory == null) {
                throw new InvalidShopRequest("Seller don't have item" + "-" + selectedItem.getName());
            }
            if (pairedInventory.getAmount() < selectedAmount) { // check if seller have enough item
                throw new InvalidShopRequest("Seller don't have enough item" + "-" + selectedItem.getName());
            }
            required_money += selectedItem.getCost();
        }
        if (!buyer.checkMoney(required_money)) {
            throw new InvalidShopRequest("Don't have enough money");
        }
    }

    private void execute(List<Inventory> sellerInventoryList, List<Inventory> buyerInventoryList, List<Inventory> selectedInventoryList, Trader buyer, Trader seller) {
        for (Inventory selectedInventory : selectedInventoryList) {
            int selectedAmount = selectedInventory.getAmount();
            Item selectedItem = selectedInventory.getDBItem().toGameItem();
            int totalCost = selectedItem.getCost() * selectedAmount;
            // operation for seller
            Inventory pairedInventoryForSeller = findInventoryFromList(sellerInventoryList, selectedInventory);//TODO: may use hashMap instead
            int leftAmount = pairedInventoryForSeller.getAmount() - selectedAmount;
            pairedInventoryForSeller.setAmount(leftAmount);
            if (leftAmount == 0) { // delete the Inventory if its amount is 0
                sellerInventoryList.remove(pairedInventoryForSeller);
                session.delete(pairedInventoryForSeller);
            }
            seller.addMoney(totalCost);
            // operation for buyer; selectedInventory contains more properties info
            Inventory pairedInventoryForBuyer = findInventoryFromList(buyerInventoryList, selectedInventory);
            if (pairedInventoryForBuyer != null) {
                pairedInventoryForBuyer.setAmount(selectedAmount + selectedInventory.getAmount());
            } else {
                Inventory createdInventory = buyer.addInventory(metaDAO, selectedInventory);
                buyerInventoryList.add(createdInventory);
            }
            buyer.subtractMoney(totalCost);
        }
    }

//    private void execute(Trader seller, Trader buyer, Map<Integer, Integer> item_list){
//        for (Map.Entry<Integer, Integer> inventory_pair : item_list.entrySet()) {
//            Inventory inventory = inventoryDAO.getInventory(inventory_pair.getKey());
//            int amount = inventory_pair.getValue();
//            // deduce the amount of item from seller
//            seller.sellItem(inventory, amount);
////            seller.
//            // sell inventory update
//            if (inventory.getAmount() == 0) { // delete record if it's amount is 0
//                session.delete(inventory);
//            }
//            // add the amount of item to buyer
//            buyer.get;
//            Inventory buy_inventory = buyer.buyItem(inventory, amount);
//            // buy inventory update
//            session.saveOrUpdate(buy_inventory);
//        }
//    }

}
