package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.Item.NormalPotion;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.*;
import edu.duke.ece.fantasy.json.InventoryRequestMessage;
import edu.duke.ece.fantasy.json.InventoryResultMessage;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InventoryHandlerTest {
    PlayerDAO mockPlayerDAO = mock(PlayerDAO.class);
    InventoryDAO mockInventoryDAO = mock(InventoryDAO.class);
    UnitDAO mockUnitDAO = mock(UnitDAO.class);
    Session mockSession = mock(Session.class);
    SoldierDAO mockSoldierDAO = mock(SoldierDAO.class);
    PlayerInventoryDAO mockPlayerInventoryDAO = mock(PlayerInventoryDAO.class);
    MetaDAO mockMetaDAO = mock(MetaDAO.class);
    InventoryHandler inventoryHandler;
    Unit testUnit = new Monster("wolf", 0, 0, 0);
    Player testPlayer = new Player();
    int testPlayerId = 1;
    int testItemId = 99;
    playerInventory selectedInventory = new playerInventory(new NormalPotion().toDBItem(), 20, testPlayer);
    List<Inventory> playerInventories = new ArrayList<>(Arrays.asList(selectedInventory));

    @BeforeEach
    void setUp() {
//        session.beginTransaction();
//        Initializer initializer = new Initializer(session);
//        initializer.initialize_test_player();
        when(mockMetaDAO.getPlayerDAO()).thenReturn(mockPlayerDAO);
        when(mockMetaDAO.getUnitDAO()).thenReturn(mockUnitDAO);
        when(mockMetaDAO.getInventoryDAO()).thenReturn(mockInventoryDAO);
        when(mockMetaDAO.getSession()).thenReturn(mockSession);
        when(mockMetaDAO.getPlayerInventoryDAO()).thenReturn(mockPlayerInventoryDAO);
        when(mockMetaDAO.getSoldierDAO()).thenReturn(mockSoldierDAO);

        when(mockUnitDAO.getUnit(anyInt())).thenReturn(testUnit);
        when(mockPlayerInventoryDAO.getInventories(testPlayer)).thenReturn(playerInventories);
        when(mockPlayerInventoryDAO.getInventory(testItemId)).thenReturn(selectedInventory);
        when(mockPlayerDAO.getPlayer(testPlayerId)).thenReturn(testPlayer);
        inventoryHandler = new InventoryHandler(mockMetaDAO);
//        when(mockPlayerDAO.getPlayer()).thenReturn();
//        when().thenReturn();
    }

    @AfterEach
    void shutdown() {
    }


    @Test
    void shouldReturnListWithListCMD() {
        InventoryRequestMessage inventoryRequestMessage = new InventoryRequestMessage();
        inventoryRequestMessage.setAction("list");
        when(mockPlayerDAO.getPlayer(testPlayerId)).thenReturn(testPlayer);

        InventoryResultMessage res = inventoryHandler.handle(inventoryRequestMessage, testPlayerId);
        assertEquals("valid", res.getResult());
    }

    @Test
    void shouldUseItemWithRightId() {
        InventoryRequestMessage inventoryRequestMessage = new InventoryRequestMessage();
        inventoryRequestMessage.setAction("use");
        inventoryRequestMessage.setInventoryID(testItemId);
        InventoryResultMessage res = inventoryHandler.handle(inventoryRequestMessage, testPlayerId);

        assertEquals("valid",res.getResult());
    }

    @Test
    void shouldNotUseItemWithWrongId(){
        InventoryRequestMessage inventoryRequestMessage = new InventoryRequestMessage();
        inventoryRequestMessage.setAction("use");
        inventoryRequestMessage.setInventoryID(testItemId-1);
        InventoryResultMessage res = inventoryHandler.handle(inventoryRequestMessage, testPlayerId);

        assertNotEquals("valid",res.getResult());
    }
}