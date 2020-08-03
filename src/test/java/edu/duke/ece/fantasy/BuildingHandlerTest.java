package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.Building.BuildingHandler;
import edu.duke.ece.fantasy.Building.BaseShop;
import edu.duke.ece.fantasy.Building.SuperShop;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.*;
import edu.duke.ece.fantasy.Building.Message.BuildingRequestMessage;
import edu.duke.ece.fantasy.Building.Message.BuildingResultMessage;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BuildingHandlerTest {
    BuildingHandler buildingHandler;
    Session session;
    private MetaDAO mockMetaDAO = mock(MetaDAO.class);
    private PlayerDAO mockPlayerDAO = mock(PlayerDAO.class);
    private TerritoryDAO mockTerritoryDAO = mock(TerritoryDAO.class);
    private DBBuildingDAO mockDBBuildingDAO = mock(DBBuildingDAO.class);
    private ShopInventoryDAO mockShopInventoryDAO = mock(ShopInventoryDAO.class);
    private int testPlayerId = 1;
    private Player testPlayer = new Player();
    private WorldCoord selectedCoord = new WorldCoord();

//    Session createSession() {
//        Session session =
//
//        return session;
//    }

    public BuildingHandlerTest() {
//        buildingHandler = new BuildingHandler(metaDAO);
//        playerDAO = new PlayerDAO(session);
//        territoryDAO = new TerritoryDAO(session);
    }


    @BeforeEach
    void setUp() {
//        session.beginTransaction();
//        Initializer initializer = new Initializer(session);
//        initializer.initialize_test_player();
        when(mockMetaDAO.getPlayerDAO()).thenReturn(mockPlayerDAO);
        when(mockMetaDAO.getTerritoryDAO()).thenReturn(mockTerritoryDAO);
        when(mockPlayerDAO.getPlayer(testPlayerId)).thenReturn(testPlayer);
        when(mockMetaDAO.getDbBuildingDAO()).thenReturn(mockDBBuildingDAO);
        when(mockMetaDAO.getShopInventoryDAO()).thenReturn(mockShopInventoryDAO);

        buildingHandler = new BuildingHandler(mockMetaDAO);
//        when(mockPlayerDAO.getPlayer()).thenReturn();
//        when().thenReturn();
    }

    @AfterEach
    void shutdown() {
    }

    @Test
    public void shouldReturnCreateListWithCorrectCoord() {
        BuildingRequestMessage requestMessage = new BuildingRequestMessage();
        requestMessage.setAction("createList");
        requestMessage.setCoord(selectedCoord);
        when(mockTerritoryDAO.getTerritory(selectedCoord)).thenReturn(new Territory(selectedCoord, 0));
//        Player player = playerDAO.getPlayer("test");
        BuildingResultMessage res = buildingHandler.handle(requestMessage, testPlayerId);
        assertEquals("success", res.getResult());
    }

    @Test
    public void shouldNotReturnCreateListWithWrongCoord() {
        BuildingRequestMessage requestMessage = new BuildingRequestMessage();
        requestMessage.setAction("createList");
        requestMessage.setCoord(selectedCoord);
        when(mockTerritoryDAO.getTerritory(selectedCoord)).thenReturn(new Territory(selectedCoord, 99));
//        Player player = playerDAO.getPlayer("test");
        BuildingResultMessage res = buildingHandler.handle(requestMessage, testPlayerId);
        assertNotEquals("success", res.getResult());
    }

    @Test
    public void shouldReturnUpdateListWithCorrectCoord() {
        BuildingRequestMessage requestMessage = new BuildingRequestMessage();
        requestMessage.setAction("upgradeList");
        requestMessage.setCoord(selectedCoord);
        when(mockDBBuildingDAO.getBuilding(selectedCoord)).thenReturn(new BaseShop().toDBBuilding());

        BuildingResultMessage res = buildingHandler.handle(requestMessage, testPlayerId);
        assertEquals("success", res.getResult());
    }

    @Test
    public void shouldReturnUpdateListWithWrongCoord() {
        BuildingRequestMessage requestMessage = new BuildingRequestMessage();
        requestMessage.setAction("upgradeList");
        requestMessage.setCoord(selectedCoord);
        when(mockDBBuildingDAO.getBuilding(selectedCoord)).thenReturn(null);

        BuildingResultMessage res = buildingHandler.handle(requestMessage, testPlayerId);
        assertNotEquals("success", res.getResult());
    }

    @Test
    public void shouldNotCreateBuildingWithWrongName() {
        BuildingRequestMessage requestMessage = new BuildingRequestMessage();
        requestMessage.setAction("create");
        requestMessage.setCoord(selectedCoord);
        requestMessage.setBuildingName("wrongName");

        BuildingResultMessage res = buildingHandler.handle(requestMessage, testPlayerId);
        assertNotEquals("success", res.getResult());
    }

    @Test
    public void shouldCreateBuildingWithRightCondition() {
        BuildingRequestMessage requestMessage = new BuildingRequestMessage();
        requestMessage.setAction("create");
        requestMessage.setCoord(selectedCoord);
        requestMessage.setBuildingName(new BaseShop().getName());

        testPlayer.setMoney(new BaseShop().getCost());

        BuildingResultMessage res = buildingHandler.handle(requestMessage, testPlayerId);
        assertEquals("success", res.getResult());
    }

    @Test
    public void shouldNotUpdateWithWrongCoord() {
        BuildingRequestMessage requestMessage = new BuildingRequestMessage();
        requestMessage.setAction("update");
        requestMessage.setCoord(selectedCoord);

        when(mockDBBuildingDAO.getBuilding(selectedCoord)).thenReturn(null);

        BuildingResultMessage res = buildingHandler.handle(requestMessage, testPlayerId);
        assertNotEquals("success", res.getResult());
    }

    @Test
    public void shouldUpgradeBuildingWithRightCondition() {
        BuildingRequestMessage requestMessage = new BuildingRequestMessage();
        requestMessage.setAction("upgrade");
        requestMessage.setCoord(selectedCoord);
        requestMessage.setBuildingName(new SuperShop().getName());

        when(mockDBBuildingDAO.getBuilding(selectedCoord)).thenReturn(new BaseShop().toDBBuilding());

        testPlayer.setMoney(new SuperShop().getCost());

        BuildingResultMessage res = buildingHandler.handle(requestMessage, testPlayerId);
        assertEquals("success", res.getResult());
    }

}