package edu.duke.ece.fantasy.net;

import edu.duke.ece.fantasy.Annotation.RequestMapping;
import edu.duke.ece.fantasy.task.MessageTask;
import edu.duke.ece.fantasy.SingleReflection;
import edu.duke.ece.fantasy.task.TaskHandler;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MessageDispatcher {

    private static final Map<Class<?>, Method> MESSAGE_METHOD = new HashMap<>();

    private Logger logger = LoggerFactory.getLogger(getClass());

    public MessageDispatcher() {
        initialize();
    }

    private void initialize() {
        Reflections reflections = SingleReflection.INSTANCE.getReflections();
        try {
            Set<Method> methods = reflections.getMethodsAnnotatedWith(RequestMapping.class);
            for (Method method : methods) {
                addToMsgMap(method);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    private void addToMsgMap(Method method) {
        for (Class<?> parameterType : method.getParameterTypes()) {
            if (Message.class.isAssignableFrom(parameterType)) {
                MESSAGE_METHOD.put(parameterType, method);
            }
        }
    }

    public void dispatch(UserSession userSession, Message msg) {
        try {
            Method method = MESSAGE_METHOD.get(msg.getClass());
            Object[] arguments = assignMethodArguments(userSession, msg, method.getParameterTypes());
            Object controller = method.getDeclaringClass().getConstructor().newInstance();
            TaskHandler.INSTANCE.addTask(new MessageTask(method,
                    controller,
                    arguments, userSession.getDistributeKey()));
        } catch (NullPointerException e) {
            logger.error("Null pointer, possible reason: doesn't have [msg,method] in map", e);
        } catch (NoSuchMethodException e) {
            logger.error("Cannot new instance", e);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            logger.error("Method arguments are wrong", e);
        }
    }

    public Object[] assignMethodArguments(UserSession userSession, Message msg, Class<?>[] methodParams) {
        Object[] result = new Object[methodParams == null ? 0 : methodParams.length];
        for (int i = 0; i < methodParams.length; i++) {
            Class<?> methodParam = methodParams[i];
            if (UserSession.class.isAssignableFrom(methodParam)) {
                result[i] = userSession;
            } else if (Message.class.isAssignableFrom(methodParam)) {
                result[i] = msg;
            }
        }
        return result;
    }

//    public void dispatch(UserSession userSession, MessagesC2S input) {
//
//        MessagesS2C result = new MessagesS2C();
//        LoginRequestMessage loginMsg = input.getLoginRequestMessage();
//        SignUpRequestMessage signupMsg = input.getSignUpRequestMessage();
//        PositionRequestMessage positionMsg = input.getPositionRequestMessage();
//        BattleRequestMessage battleMsg = input.getBattleRequestMessage();
//        AttributeRequestMessage attributeMsg = input.getAttributeRequestMessage();
//        ShopRequestMessage shopRequestMessage = input.getShopRequestMessage();
//        InventoryRequestMessage inventoryRequestMessage = input.getInventoryRequestMessage();
//        BuildingRequestMessage buildingRequestMessage = input.getBuildingRequestMessage();
//        LevelUpRequestMessage levelUpMsg = input.getLevelUpRequestMessage();
//        RedirectMessage redirectMsg = input.getRedirectMessage();
//        ReviveRequestMessage reviveMsg = input.getReviveRequestMessage();
//        FriendRequestMessage friendRequestMessage = input.getFriendRequestMessage();
//
//        // set redirectMsg and player's status
//        if (redirectMsg != null) {
//            if (userSession.getPlayer() != null) {
//                userSession.getPlayer().setStatus(redirectMsg.getDestination());
//            }
//            result.setRedirectMessage(redirectMsg);
//        }
////
//        if (friendRequestMessage != null) {
//            FriendHandler fh = new FriendHandler();
//            result.setFriendResultMessage(fh.handle(userSession.getPlayer().getId(), friendRequestMessage));
//        }
////
//        if (reviveMsg != null) {
//            ReviveHandler rh = new ReviveHandler();
//            result.setReviveResultMessage(rh.handle(userSession.getPlayer().getId()));
//        }
//
//        if (loginMsg != null) {
//            // if login succeed, sharedData will hold login-player's info
//            LoginHandler lh = new LoginHandler();
//            result.setLoginResultMessage(lh.handle(loginMsg));
//        }
//
//        if (signupMsg != null) {
//            SignUpHandler sh = new SignUpHandler();
//            result.setSignUpResultMessage(sh.handle(signupMsg));
//        }
////
//        if (positionMsg != null) {
//            PositionUpdateHandler positionUpdateHandler = new PositionUpdateHandler();
//            result.setPositionResultMessage(positionUpdateHandler.handle(userSession.getPlayer(), positionMsg));
//            // received currentCoord in the request only hold x/y_coord, not hold wid
//            // we add wid to it
//            WorldCoord currentCoord = positionMsg.getCurrentCoord();
//            currentCoord.setWid(userSession.getPlayer().getCurWorldId());
//            // update player info in sharedData between taskScheduler and messageHandler
//            userSession.getPlayer().setCurrentCoord(currentCoord);
//        }
//
//        if (battleMsg != null) {
//            //TODO : battleHandler should store unitQueue in database
//
//            // add wid to the received currentCoord in the request
////            if (battleMsg.getTerritoryCoord() != null) battleMsg.getTerritoryCoord().setWid(userSession.getPlayer().getCurWorldId());
////            BattleResultMessage battleResult = myBattleHandler.handle(battleMsg, userSession.getPlayer().getId(), metaDAO);
////            result.setBattleResultMessage(battleResult);
////            if(battleResult.getResult().equals("lose")) userSession.getPlayer().setStatus(WorldInfo.DeathWorld);
//        }
//
//        if (levelUpMsg != null) {
//            LevelUpHandler luh = new LevelUpHandler();
//            result.setLevelUpResultMessage(luh.handle(levelUpMsg));
//        }
//
//        if (attributeMsg != null) {
//            AttributeHandler ah = new AttributeHandler();
//            result.setAttributeResultMessage(ah.handle(attributeMsg, userSession.getPlayer().getId()));
//        }
//
//        if (shopRequestMessage != null) {
//            ShopHandler shopHandler = new ShopHandler();
//            InventoryHandler inventoryHandler = new InventoryHandler();
//
//            if (shopRequestMessage.getCoord() != null)
//                shopRequestMessage.getCoord().setWid(userSession.getPlayer().getCurWorldId());
//            ShopResultMessage shopResultMessage = shopHandler.handle(shopRequestMessage, userSession.getPlayer().getId());
//            // add inventory result to shop
//            InventoryRequestMessage shopInventoryRequestMessage = new InventoryRequestMessage();
//            shopInventoryRequestMessage.setAction("list");
//
//            result.setInventoryResultMessage(inventoryHandler.handle(shopInventoryRequestMessage, userSession.getPlayer().getId()));
//            result.setShopResultMessage(shopResultMessage);
//        }
//
//        if (inventoryRequestMessage != null) {
//            InventoryHandler inventoryHandler = new InventoryHandler();
//            result.setInventoryResultMessage(inventoryHandler.handle(inventoryRequestMessage, userSession.getPlayer().getId()));
//            AttributeRequestMessage attributeRequestMessage = new AttributeRequestMessage();
//            result.setAttributeResultMessage((new AttributeHandler()).handle(attributeRequestMessage, userSession.getPlayer().getId()));
//        }
//
//        if (buildingRequestMessage != null) {
//            BuildingHandler buildingHandler = new BuildingHandler();
//            if (buildingRequestMessage.getCoord() != null)
//                buildingRequestMessage.getCoord().setWid(userSession.getPlayer().getCurWorldId());
//            result.setBuildingResultMessage(buildingHandler.handle(buildingRequestMessage, userSession.getPlayer().getId()));
//        }
//
//        userSession.sendMsg(result);
//    }

}

