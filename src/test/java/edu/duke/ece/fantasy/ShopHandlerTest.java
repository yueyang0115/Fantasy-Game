package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.Building.ShopHandler;
import edu.duke.ece.fantasy.Item.NormalPotion;
import edu.duke.ece.fantasy.Item.SuperPotion;
import edu.duke.ece.fantasy.Building.BaseShop;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.*;
import edu.duke.ece.fantasy.Building.Message.ShopRequestMessage;
import edu.duke.ece.fantasy.Building.Message.ShopResultMessage;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//
class ShopHandlerTest {
    ShopHandler shopHandler;
    private PlayerDAO mockPlayerDAO;
    private Player playerWithEnoughMoney = mock(Player.class);
    private Player playerWithoutEnoughMoney = mock(Player.class);
    private int testPlayerId = 1;
    private DBBuildingDAO mockDbBuildingDAO;
    private InventoryDAO mockInventoryDAO;
    private Session mockSession;
    private ShopInventoryDAO mockShopInventoryDAO;
    private UnitDAO mockUnitDAO;
    private MetaDAO mockMetaDAO;
    private PlayerInventoryDAO mockPlayerInventoryDAO;
    private Inventory inventory1 = new Inventory(new NormalPotion().toDBItem(), 20);
    private Inventory inventory2 = new Inventory(new SuperPotion().toDBItem(), 20);
    private List<Inventory> shopInventories = new ArrayList<>(Arrays.asList(inventory1));
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
        when(mockPlayerInventoryDAO.getInventories(any())).thenReturn(new ArrayList<>());
        when(playerWithEnoughMoney.checkMoney(anyInt())).thenReturn(true);
        when(playerWithoutEnoughMoney.checkMoney(anyInt())).thenReturn(false);

        for (int i = 0; i < shopInventories.size(); i++) { // set up id for each inventory
            shopInventories.get(i).setId(i);
            when(mockInventoryDAO.getInventory(i)).thenReturn(shopInventories.get(i));
        }
    }

    @AfterEach
    void shutdown() {
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
        when(mockPlayerDAO.getPlayer(testPlayerId)).thenReturn(playerWithEnoughMoney);

        shopHandler = new ShopHandler(mockMetaDAO);

        List<Inventory> selectedInventoryList = new ArrayList<>();
        for (Inventory inventory : shopInventories) {
            selectedInventoryList.add(new Inventory(inventory.getDBItem(), inventory.getAmount()));
        }

        ShopResultMessage res = shopHandler.handle(generateShopBuyRequest(selectedInventoryList), testPlayerId);
        for (int i = 0; i < res.getItems().size(); i++) {
            assertEquals(0, res.getItems().get(i).getAmount());
        }
    }

    @Test
    void shopInventoryShouldNotDecreaseWhenPlayerDontHaveEnoughMoney() {
        when(mockPlayerDAO.getPlayer(testPlayerId)).thenReturn(playerWithoutEnoughMoney);

        shopHandler = new ShopHandler(mockMetaDAO);

        List<Inventory> selectedInventoryList = new ArrayList<>();
        for (Inventory inventory : shopInventories) {
            selectedInventoryList.add(new Inventory(inventory.getDBItem(), inventory.getAmount()));
        }

        ShopResultMessage res = shopHandler.handle(generateShopBuyRequest(selectedInventoryList), testPlayerId);
        for (int i = 0; i < res.getItems().size(); i++) {
            assertEquals(shopInventories.get(i).getAmount(), res.getItems().get(i).getAmount());
        }
    }

    @Test
    void shopInventoryShouldNotDecreaseWhenPlayerBuyMoreItem() {
        when(mockPlayerDAO.getPlayer(testPlayerId)).thenReturn(playerWithoutEnoughMoney);

        shopHandler = new ShopHandler(mockMetaDAO);

        List<Inventory> selectedInventoryList = new ArrayList<>();
        for (Inventory inventory : shopInventories) {
            selectedInventoryList.add(new Inventory(inventory.getDBItem(), inventory.getAmount() + 1));
        }

        ShopResultMessage res = shopHandler.handle(generateShopBuyRequest(selectedInventoryList), testPlayerId);
        for (int i = 0; i < res.getItems().size(); i++) {
            assertEquals(shopInventories.get(i).getAmount(), res.getItems().get(i).getAmount());
        }
    }


    ShopRequestMessage generateShopBuyRequest(List<Inventory> selectedInventoryList) {
        ShopRequestMessage shopRequestMessage = new ShopRequestMessage();
        shopRequestMessage.setCoord(shopCoord);
        shopRequestMessage.setAction("buy");
        shopRequestMessage.setSelectedItems(selectedInventoryList);
        return shopRequestMessage;
    }

}