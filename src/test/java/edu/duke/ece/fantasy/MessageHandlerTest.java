package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.json.*;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageHandlerTest {
    //@Test
    void testAll(){
        //testLogin();
        //testSignUp();
    }

//    @Test
    void testWid(){
        MessageHandler mh = new MessageHandler();
        LoginRequestMessage loginRequest = new LoginRequestMessage();
        loginRequest.setUsername("4321");
        loginRequest.setPassword("4321");
        MessagesC2S request = new MessagesC2S(loginRequest);
        MessagesS2C result = mh.handle(request);
    }

    void testLogin(){
        MessageHandler mh = new MessageHandler();
        LoginRequestMessage loginRequest = new LoginRequestMessage();
        loginRequest.setUsername("00");
        loginRequest.setPassword("11");
        MessagesC2S request = new MessagesC2S(loginRequest);
        MessagesS2C result = mh.handle(request);
        assertEquals(result.getSignUpResultMessage(),null);
        assertEquals(result.getPositionResultMessage(),null);
        assertEquals(result.getLoginResultMessage().getStatus(),"fail");
        assertNotNull(result.getLoginResultMessage().getError_msg());
    }

    void testSignUp(){
        MessageHandler mh = new MessageHandler();
        SignUpRequestMessage signUpRequest = new SignUpRequestMessage();
        signUpRequest.setUsername("00");
        signUpRequest.setPassword("11");
        MessagesC2S request = new MessagesC2S(signUpRequest);
        MessagesS2C result = mh.handle(request);
        assertEquals(result.getLoginResultMessage(),null);
        assertEquals(result.getPositionResultMessage(),null);
        assertEquals(result.getSignUpResultMessage().getStatus(),"success");
        assertEquals(result.getSignUpResultMessage().getError_msg(),null);
    }
}
