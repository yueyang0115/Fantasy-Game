package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.DAO.PlayerDAO;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.WorldCoord;
import edu.duke.ece.fantasy.json.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LoginHandlerTest {
    MetaDAO mockedMetaDAO = mock(MetaDAO.class);
    PlayerDAO mockedPlayerDAO = mock(PlayerDAO.class);

    public LoginHandlerTest(){
        when(mockedMetaDAO.getPlayerDAO()).thenReturn(mockedPlayerDAO);

        doNothing().when(mockedPlayerDAO).addPlayer(anyString(),anyString());
    }

    @Test
    public void testAll(){
        testLoginSuccess();
        testLoginFail();
        testMessageHandler();
    }

    public void testLoginSuccess(){
        when(mockedPlayerDAO.getPlayer("mockName","mockPassword"))
                .thenReturn(new Player("mockName","mockPassword"));

        SharedData sharedData = new SharedData();
        LoginHandler lh = new LoginHandler(mockedMetaDAO, sharedData);
        LoginRequestMessage request = new LoginRequestMessage("mockName","mockPassword");
        LoginResultMessage result = lh.handle(request);
        assertEquals(result.getStatus(),"success");
    }

    public void testLoginFail(){
        when(mockedPlayerDAO.getPlayer("mockName","mockPassword"))
                .thenReturn(null);

        SharedData sharedData = new SharedData();
        LoginHandler lh = new LoginHandler(mockedMetaDAO, sharedData);
        LoginRequestMessage request = new LoginRequestMessage("mockName","mockPassword");
        LoginResultMessage result = lh.handle(request);
        assertEquals(result.getStatus(),"fail");
    }

    public void testMessageHandler(){
        Player player = new Player("mockName","mockPassword");
        player.setWid(1);
        player.setId(2);
        when(mockedPlayerDAO.getPlayer("mockName","mockPassword"))
                .thenReturn(player);

        SharedData sharedData = new SharedData();
        assertEquals(sharedData.getPlayer(), null);
        MessageHandler mh = new MessageHandler(mockedMetaDAO, sharedData);
        LoginRequestMessage loginRequest = new LoginRequestMessage("mockName","mockPassword");
        MessagesC2S request = new MessagesC2S(loginRequest);
        MessagesS2C result = mh.handle(request);
        assertEquals(result.getLoginResultMessage().getStatus(),"success");
        assertEquals(sharedData.getPlayer().getId(),2);
        assertEquals(sharedData.getPlayer().getWid(),1);
    }
}

