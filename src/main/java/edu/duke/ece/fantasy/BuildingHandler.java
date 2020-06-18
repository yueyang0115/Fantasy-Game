//package edu.duke.ece.fantasy;
//
//import edu.duke.ece.fantasy.database.BuildingDAO;
//import edu.duke.ece.fantasy.database.PlayerDAO;
//import edu.duke.ece.fantasy.database.Shop;
//import edu.duke.ece.fantasy.database.ShopDAO;
//import edu.duke.ece.fantasy.json.BuildingRequestMessage;
//import edu.duke.ece.fantasy.json.BuildingResultMessage;
//import org.hibernate.Session;
//
//public class BuildingHandler {
//    PlayerDAO playerDAO;
//    BuildingDAO buildingDAO;
//    ShopDAO shopDAO;
//
//
//    public BuildingHandler(Session session) {
//        playerDAO = new PlayerDAO(session);
//        buildingDAO = new BuildingDAO(session);
//        shopDAO = new ShopDAO(session);
//    }
//
//    public BuildingResultMessage handle(BuildingRequestMessage buildingRequestMessage, int playerId) {
//        BuildingResultMessage buildingResultMessage = new BuildingResultMessage();
//        String action = buildingRequestMessage.getAction();
//
//        if (action.equals("createList")) {
//            buildingResultMessage.addBuilding("Shop");
//
//        } else if (action.equals("create")) {
//            // world coord
//            try{
//
//                shopDAO.createShop();
//            }
//        }
//
//        return buildingResultMessage;
//    }
//}
