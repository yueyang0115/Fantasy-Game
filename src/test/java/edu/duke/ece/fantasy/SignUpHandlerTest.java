package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.DAO.PlayerDAO;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.json.SignUpRequestMessage;
import edu.duke.ece.fantasy.json.SignUpResultMessage;
import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

//@RunWith(MockitoJUnitRunner.class)
public class SignUpHandlerTest {
    MetaDAO mockedMetaDAO = mock(MetaDAO.class);
    PlayerDAO mockedPlayerDAO = mock(PlayerDAO.class);

    public SignUpHandlerTest(){
        when(mockedMetaDAO.getPlayerDAO()).thenReturn(mockedPlayerDAO);
        when(mockedPlayerDAO.getPlayer(anyString())).thenReturn(null);
        doNothing().when(mockedPlayerDAO).addPlayer(anyString(),anyString());
    }

    @Test
    public void testAll(){

        SignUpHandler sh = new SignUpHandler(mockedMetaDAO);
        SignUpRequestMessage request = new SignUpRequestMessage("mockName","mockPassword");
        SignUpResultMessage result = sh.handle(request);
        assertEquals(result.getStatus(),"success");
    }

}