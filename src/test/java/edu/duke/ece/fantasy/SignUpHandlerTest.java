package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.Account.Message.SignUpRequestMessage;
import edu.duke.ece.fantasy.Account.Message.SignUpResultMessage;
import edu.duke.ece.fantasy.Account.SignUpHandler;
import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.DAO.PlayerDAO;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.json.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SignUpHandlerTest {
    MetaDAO mockedMetaDAO = mock(MetaDAO.class);
    PlayerDAO mockedPlayerDAO = mock(PlayerDAO.class);

    public SignUpHandlerTest(){
        when(mockedMetaDAO.getPlayerDAO()).thenReturn(mockedPlayerDAO);
        doNothing().when(mockedPlayerDAO).addPlayer(anyString(),anyString());
    }

    @Test
    public void testAll(){
        testSignUpSuccess();
        testSignUpFail();
        testMessageHandler();
    }

    public void testSignUpSuccess(){
        when(mockedPlayerDAO.getPlayer(anyString())).thenReturn(null);

        SignUpHandler sh = new SignUpHandler(mockedMetaDAO);
        SignUpRequestMessage request = new SignUpRequestMessage("mockName","mockPassword");
        SignUpResultMessage result = sh.handle(request);
        assertEquals(result.getStatus(),"success");
    }

    public void testSignUpFail(){
        when(mockedPlayerDAO.getPlayer(anyString())).thenReturn(new Player());

        SignUpHandler sh = new SignUpHandler(mockedMetaDAO);
        SignUpRequestMessage request = new SignUpRequestMessage("mockName","mockPassword");
        SignUpResultMessage result = sh.handle(request);
        assertEquals(result.getStatus(),"fail");
    }

    public void testMessageHandler(){
        when(mockedPlayerDAO.getPlayer(anyString())).thenReturn(null);

        SharedData sharedData = new SharedData();
        MessageHandler mh = new MessageHandler(mockedMetaDAO, sharedData);
        SignUpRequestMessage SignUpRequest = new SignUpRequestMessage("mockName","mockPassword");
        MessagesC2S request = new MessagesC2S(SignUpRequest);
        MessagesS2C result = mh.handle(request);
        assertEquals(result.getSignUpResultMessage().getStatus(),"success");
    }

}