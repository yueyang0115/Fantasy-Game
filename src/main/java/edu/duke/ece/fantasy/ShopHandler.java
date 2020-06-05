package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.Shop;
import edu.duke.ece.fantasy.database.ShopDAO;
import edu.duke.ece.fantasy.json.BattleResultMessage;
import edu.duke.ece.fantasy.json.ShopRequestMessage;
import edu.duke.ece.fantasy.json.ShopResultMessage;
import org.hibernate.Session;

public class ShopHandler {
    private ShopDAO shopDAO;

    public ShopHandler(Session session){
        shopDAO = new ShopDAO(session);
    }

    public ShopResultMessage handle(ShopRequestMessage request){
        String action = request.getAction();
        Shop shop = shopDAO.getShop(request.getShopID());
        // may need to check relationship of shop and territory
        ShopResultMessage result = new ShopResultMessage();
        if(action.equals("list")){
            result.setItems(shop.getInventory());
            result.setResult("valid");
        } else if(action.equals("buy")){

        } else{

        }
        return result;
    }

}
