package edu.duke.ece.fantasy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Territory;
import edu.duke.ece.fantasy.json.*;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SignUpLogInHandlerTest {
    private SignUpHandler sh;
    private LoginHandler lh;
    Session session;

    Session createSession() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        return session;
    }

    public SignUpLogInHandlerTest(){
        this.session = createSession();
        this.sh = new SignUpHandler(this.session);
        this.lh = new LoginHandler(this.session);
    }

    @Test
    public void testAll() {
        try {
            session.beginTransaction();
            testSignUpSuccess();
            testSignUpFail();
            testLogInSuccess();
            testLogInFail();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //HibernateUtil.shutdown();
        }
    }

    private void testSignUpSuccess(){
        SignUpRequestMessage request = new SignUpRequestMessage();
        request.setPassword("111");
        request.setUsername("111");
        SignUpResultMessage SignUpResult = sh.handle(request);
        assertEquals(SignUpResult.getStatus(),"success");
        assertEquals(SignUpResult.getError_msg(),null);
    }

    private void testSignUpFail(){
        SignUpRequestMessage request = new SignUpRequestMessage();
        request.setPassword("111");
        request.setUsername("111");
        SignUpResultMessage SignUpResult = sh.handle(request);
        assertEquals(SignUpResult.getStatus(),"fail");
        assertNotNull(SignUpResult.getError_msg());
    }

    private void testLogInSuccess(){
        LoginRequestMessage request = new LoginRequestMessage();
        request.setPassword("111");
        request.setUsername("111");
        LoginResultMessage LogInResult = lh.handle(request);
        assertEquals(LogInResult.getStatus(),"success");
        assertEquals(LogInResult.getError_msg(),null);
        assertNotNull(LogInResult.getWid());
    }

    private void testLogInFail(){
        LoginRequestMessage request = new LoginRequestMessage();
        request.setPassword("1");
        request.setUsername("111");
        LoginResultMessage LogInResult = lh.handle(request);
        assertEquals(LogInResult.getStatus(),"fail");
        assertNotNull(LogInResult.getError_msg());
    }
}
