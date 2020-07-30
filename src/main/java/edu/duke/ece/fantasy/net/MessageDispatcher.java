package edu.duke.ece.fantasy.net;

import edu.duke.ece.fantasy.*;
import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.WorldCoord;
import edu.duke.ece.fantasy.database.WorldInfo;
import edu.duke.ece.fantasy.json.*;
import org.hibernate.Session;

public class MessageDispatcher {
    private UserSession userSession;
    private MessagesC2S request;

    public MessageDispatcher(UserSession userSession, MessagesC2S request) {
        this.userSession = userSession;
        this.request = request;
    }

    public void dispatch(MessagesC2S input) {
        MessagesS2C result = new MessagesS2C();
        LoginRequestMessage loginMsg = input.getLoginRequestMessage();
        SignUpRequestMessage signupMsg = input.getSignUpRequestMessage();
        PositionRequestMessage positionMsg = input.getPositionRequestMessage();
        BattleRequestMessage battleMsg = input.getBattleRequestMessage();
        AttributeRequestMessage attributeMsg = input.getAttributeRequestMessage();
        ShopRequestMessage shopRequestMessage = input.getShopRequestMessage();
        InventoryRequestMessage inventoryRequestMessage = input.getInventoryRequestMessage();
        BuildingRequestMessage buildingRequestMessage = input.getBuildingRequestMessage();
        LevelUpRequestMessage levelUpMsg = input.getLevelUpRequestMessage();
        RedirectMessage redirectMsg = input.getRedirectMessage();
        ReviveRequestMessage reviveMsg = input.getReviveRequestMessage();
        FriendRequestMessage friendRequestMessage = input.getFriendRequestMessage();

        Session session = HibernateUtil.getSessionFactory().openSession();
        MetaDAO metaDAO = new MetaDAO(session);
        session.beginTransaction();

        // set redirectMsg and player's status
        if(redirectMsg != null){
            if(userSession.getPlayer() != null) userSession.getPlayer().setStatus(redirectMsg.getDestination());
            result.setRedirectMessage(redirectMsg);
        }

        if(friendRequestMessage != null){
            FriendHandler fh = new FriendHandler(metaDAO);
            result.setFriendResultMessage(fh.handle(userSession.getPlayer().getId(),friendRequestMessage));
        }

        if(reviveMsg != null){
            ReviveHandler rh = new ReviveHandler(metaDAO);
            result.setReviveResultMessage(rh.handle(userSession.getPlayer().getId()));
        }

        if (loginMsg != null) {
            // if login succeed, sharedData will hold login-player's info
            LoginHandler lh = new LoginHandler(metaDAO, userSession);
            result.setLoginResultMessage(lh.handle(loginMsg));
        }

        if (signupMsg != null) {
            SignUpHandler sh = new SignUpHandler(metaDAO);
            result.setSignUpResultMessage(sh.handle(signupMsg));
        }

        if (positionMsg != null) {
            PositionUpdateHandler positionUpdateHandler = new PositionUpdateHandler(metaDAO);
            result.setPositionResultMessage(positionUpdateHandler.handle(userSession.getPlayer(), positionMsg));
            // received currentCoord in the request only hold x/y_coord, not hold wid
            // we add wid to it
            WorldCoord currentCoord  = positionMsg.getCurrentCoord();
            currentCoord.setWid(userSession.getPlayer().getCurWorldId());
            // update player info in sharedData between taskScheduler and messageHandler
            userSession.getPlayer().setCurrentCoord(currentCoord);
        }

        if (battleMsg != null) {
            //TODO : battleHandler should store unitQueue in database

            // add wid to the received currentCoord in the request
//            if (battleMsg.getTerritoryCoord() != null) battleMsg.getTerritoryCoord().setWid(userSession.getPlayer().getCurWorldId());
//            BattleResultMessage battleResult = myBattleHandler.handle(battleMsg, userSession.getPlayer().getId(), metaDAO);
//            result.setBattleResultMessage(battleResult);
//            if(battleResult.getResult().equals("lose")) userSession.getPlayer().setStatus(WorldInfo.DeathWorld);
        }

        if(levelUpMsg != null){
            LevelUpHandler luh = new LevelUpHandler(metaDAO);
            result.setLevelUpResultMessage(luh.handle(levelUpMsg));
        }

        if (attributeMsg != null) {
            AttributeHandler ah = new AttributeHandler(metaDAO);
            result.setAttributeResultMessage(ah.handle(attributeMsg, userSession.getPlayer().getId()));
        }

        if (shopRequestMessage != null) {
            ShopHandler shopHandler = new ShopHandler(metaDAO);
            InventoryHandler inventoryHandler = new InventoryHandler(metaDAO);

            if (shopRequestMessage.getCoord() != null) shopRequestMessage.getCoord().setWid(userSession.getPlayer().getCurWorldId());
            ShopResultMessage shopResultMessage = shopHandler.handle(shopRequestMessage, userSession.getPlayer().getId());
            // add inventory result to shop
            InventoryRequestMessage shopInventoryRequestMessage = new InventoryRequestMessage();
            shopInventoryRequestMessage.setAction("list");

            result.setInventoryResultMessage(inventoryHandler.handle(shopInventoryRequestMessage, userSession.getPlayer().getId()));
            result.setShopResultMessage(shopResultMessage);
        }

        if(inventoryRequestMessage != null){
            InventoryHandler inventoryHandler = new InventoryHandler(metaDAO);
            result.setInventoryResultMessage(inventoryHandler.handle(inventoryRequestMessage, userSession.getPlayer().getId()));
            AttributeRequestMessage attributeRequestMessage = new AttributeRequestMessage();
            result.setAttributeResultMessage((new AttributeHandler(metaDAO)).handle(attributeRequestMessage, userSession.getPlayer().getId()));
        }

        if (buildingRequestMessage != null) {
            BuildingHandler buildingHandler = new BuildingHandler(metaDAO);
            if (buildingRequestMessage.getCoord() != null) buildingRequestMessage.getCoord().setWid(userSession.getPlayer().getCurWorldId());
            result.setBuildingResultMessage(buildingHandler.handle(buildingRequestMessage, userSession.getPlayer().getId()));
        }

        session.getTransaction().commit();
        sendMsg(result);
    }

    public void sendMsg(MessagesS2C result){
        userSession.getChannel().writeAndFlush(result);
    }

}

