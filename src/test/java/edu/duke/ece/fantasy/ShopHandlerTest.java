package edu.duke.ece.fantasy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.Item.Item;
import edu.duke.ece.fantasy.Item.Potion;
import edu.duke.ece.fantasy.building.BaseShop;
import edu.duke.ece.fantasy.building.Building;
import edu.duke.ece.fantasy.building.Shop;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.*;
import edu.duke.ece.fantasy.json.ShopRequestMessage;
import edu.duke.ece.fantasy.json.ShopResultMessage;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//
class ShopHandlerTest {
    ShopHandler shopHandler;
    private PlayerDAO mockPlayerDAO;
    private Player playerWithEnoughMoney = mock(Player.class);
    private int testPlayerId = 1;
    private DBBuildingDAO mockDbBuildingDAO;
    private InventoryDAO mockInventoryDAO;
    private Session mockSession;
    private ShopInventoryDAO mockShopInventoryDAO;
    private UnitDAO mockUnitDAO;
    private MetaDAO mockMetaDAO;
    private PlayerInventoryDAO mockPlayerInventoryDAO;
    private BaseShop baseShop = new BaseShop();
    private int item1Amount = 20;
    private DBItem item1 = new Potion().toDBItem();
    private shopInventory inventory1 = new shopInventory(item1, item1Amount);
    private List<shopInventory> shopInventories = Arrays.asList(inventory1);
    ObjectMapper objectMapper = new ObjectMapper();
    Logger logger = LoggerFactory.getLogger(ShopHandlerTest.class);
    ShopInventoryDAO shopInventoryDAO;
    WorldCoord shopCoord = new WorldCoord();

    @BeforeEach
    void setUp() {
        mockMetaDAO = mock(MetaDAO.class);
        mockPlayerDAO = mock(PlayerDAO.class);
        mockDbBuildingDAO = mock(DBBuildingDAO.class);
        mockInventoryDAO = mock(InventoryDAO.class);
        mockShopInventoryDAO = mock(ShopInventoryDAO.class);
        mockUnitDAO = mock(UnitDAO.class);
        mockPlayerInventoryDAO = mock(PlayerInventoryDAO.class);
        mockSession = mock(Session.class);

        when(mockMetaDAO.getPlayerDAO()).thenReturn(mockPlayerDAO);
        when(mockMetaDAO.getDbBuildingDAO()).thenReturn(mockDbBuildingDAO);
        when(mockMetaDAO.getInventoryDAO()).thenReturn(mockInventoryDAO);
        when(mockMetaDAO.getInventoryDAO()).thenReturn(mockInventoryDAO);
        when(mockMetaDAO.getShopInventoryDAO()).thenReturn(mockShopInventoryDAO);
        when(mockMetaDAO.getUnitDAO()).thenReturn(mockUnitDAO);
        when(mockMetaDAO.getPlayerInventoryDAO()).thenReturn(mockPlayerInventoryDAO);
        when(mockMetaDAO.getSession()).thenReturn(mockSession);

        when(mockDbBuildingDAO.getBuilding(shopCoord)).thenReturn(new BaseShop().toDBBuilding());
        when(mockShopInventoryDAO.getInventories(shopCoord)).thenReturn(shopInventories);
        when(playerWithEnoughMoney.checkMoney(anyInt())).thenReturn(true);

        for (int i = 0; i < shopInventories.size(); i++) { // set up id for each inventory
            shopInventories.get(i).setId(i);
            when(mockInventoryDAO.getInventory(i)).thenReturn(shopInventories.get(i));
        }
    }

    @AfterEach
    void shutdown(){
//        setUp();
    }


    @Test
    void shouldReturnListWithExistingCoord() {
        ShopRequestMessage shopRequestMessage = new ShopRequestMessage();
        shopRequestMessage.setCoord(shopCoord);
        shopRequestMessage.setAction("list");

        shopHandler = new ShopHandler(mockMetaDAO);
        ShopResultMessage res = shopHandler.handle(shopRequestMessage, 0);
//        assertEquals("valid", res.getResult());
        List<Inventory> resultShopInventories = res.getItems();
        for (int i = 0; i < shopInventories.size(); i++) {
            assertEquals(resultShopInventories.get(i).getAmount(), shopInventories.get(i).getAmount());
            assertEquals(resultShopInventories.get(i).getDBItem().toGameItem(), shopInventories.get(i).getDBItem().toGameItem());
        }
        res.getItems();
    }

    @Test
    void shopInventoryShouldDecreaseWhenPlayerBuySucceed() {
        playerWithEnoughMoney.setMoney(item1Amount * item1.toGameItem().getCost());
        when(mockPlayerDAO.getPlayer(testPlayerId)).thenReturn(playerWithEnoughMoney);

        List<shopInventory> inventories = mockShopInventoryDAO.getInventories(shopCoord);
        shopHandler = new ShopHandler(mockMetaDAO);

        Map<Integer, Integer> itemMap = new HashMap<>();
        itemMap.put(inventory1.getId(), item1Amount);

        ShopResultMessage res = shopHandler.handle(generateShopBuyRequest(itemMap), testPlayerId);
        for (int i = 0; i < res.getItems().size(); i++) {
            assertEquals(0,res.getItems().get(i).getAmount());
        }
    }


    ShopRequestMessage generateShopBuyRequest(Map<Integer, Integer> itemMap) {
        ShopRequestMessage shopRequestMessage = new ShopRequestMessage();
        shopRequestMessage.setCoord(shopCoord);
        shopRequestMessage.setAction("buy");
        shopRequestMessage.setItemMap(itemMap);
        return shopRequestMessage;
    }

//    void handle_buy() {
////        List<shopInventory> itemPacks = new ArrayList<>(DBShop.getItems());
//        List<shopInventory> itemPacks = shopInventoryDAO.getInventories(shopCoord);
//
//        for (int i = 0; i < itemPacks.size(); i++) {
//            try {
//                shopInventory select_item = itemPacks.get(i);
//                Item item_obj = select_item.getDBItem().toGameItem();
//                int itemPack_id = select_item.getId();
//                int item_amount = select_item.getAmount();
//                int required_money = item_obj.getCost() * item_amount;
//                Player player = playerDAO.getPlayer("test");
//
//                // Don't have enough money
//                player.setMoney(required_money - 1);
//                ShopResultMessage resultMessage = buy_item(player, itemPack_id, item_amount);
//                assertEquals(item_amount, resultMessage.getItems().get(0).getAmount());
//                assertNotEquals("valid", resultMessage.getResult());
//
//                // shop don't have enough item
//                player.setMoney(required_money);
//                buy_item(player, itemPack_id, item_amount + 1);
//                assertEquals(item_amount, resultMessage.getItems().get(0).getAmount());
//                assertNotEquals("valid", resultMessage.getResult());
//
//                // success
//                player.setMoney(required_money);
//                resultMessage = buy_item(player, itemPack_id, item_amount - 1);
//                assertEquals("valid", resultMessage.getResult());
//                try {
//                    logger.info(objectMapper.writeValueAsString(resultMessage));
//                } catch (JsonProcessingException e) {
//                    e.printStackTrace();
//                }
//
//                // success buy again
//                player.setMoney(required_money);
//                resultMessage = buy_item(player, itemPack_id, 1);
//
//                assertEquals("valid", resultMessage.getResult());
//                try {
//                    logger.info(objectMapper.writeValueAsString(resultMessage));
//                } catch (JsonProcessingException e) {
//                    e.printStackTrace();
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//    }

    ShopResultMessage buy_item(Player player, int inventory_id, int item_amount) {
        ShopRequestMessage shopRequestMessage = new ShopRequestMessage();
        shopRequestMessage.setCoord(shopCoord);
        shopRequestMessage.setAction("buy");
        Map<Integer, Integer> itemMap = new HashMap<>();
        itemMap.put(inventory_id, item_amount);
        shopRequestMessage.setItemMap(itemMap);
        return shopHandler.handle(shopRequestMessage, player.getId());
    }
}