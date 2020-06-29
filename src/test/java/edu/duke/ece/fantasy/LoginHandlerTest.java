package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.DAO.PlayerDAO;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.json.LoginRequestMessage;
import edu.duke.ece.fantasy.json.LoginResultMessage;
import edu.duke.ece.fantasy.json.SignUpRequestMessage;
import edu.duke.ece.fantasy.json.SignUpResultMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LoginHandlerTest {
    MetaDAO mockedMetaDAO = mock(MetaDAO.class);
    PlayerDAO mockedPlayerDAO = mock(PlayerDAO.class);

    public LoginHandlerTest(){
        when(mockedMetaDAO.getPlayerDAO()).thenReturn(mockedPlayerDAO);
        when(mockedPlayerDAO.getPlayer("mockName","mockPassword"))
                .thenReturn(new Player("mockName","mockPassword"));
        doNothing().when(mockedPlayerDAO).addPlayer(anyString(),anyString());
    }

    @Test
    public void testAll(){
        SharedData sharedData = new SharedData();
        LoginHandler lh = new LoginHandler(mockedMetaDAO, sharedData);
        LoginRequestMessage request = new LoginRequestMessage("mockName","mockPassword");
        LoginResultMessage result = lh.handle(request);
        assertEquals(result.getStatus(),"success");
    }
}

