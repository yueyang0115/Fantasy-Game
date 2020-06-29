package edu.duke.ece.fantasy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.database.DAO.MetaDAO;
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
        MetaDAO metaDAO = new MetaDAO(session);
        SharedData sharedData = new SharedData();
        this.sh = new SignUpHandler(metaDAO);
        this.lh = new LoginHandler(metaDAO, sharedData);
    }

    //@Test
    public void testAll() {
        try {
            session.beginTransaction();
            testSignUpSuccess("111","111");
            testSignUpFail("111","111");
            testSignUpFail("111","11");
            testSignUpSuccess("112","11");
            testSignUpFail("112","11");
            testLogInSuccess();
            testLogInFail();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //HibernateUtil.shutdown();
        }
    }

    private void testSignUpSuccess(String username,String password){
        SignUpRequestMessage request = new SignUpRequestMessage();
        request.setPassword(password);
        request.setUsername(username);
        SignUpResultMessage SignUpResult = sh.handle(request);
        assertEquals(SignUpResult.getStatus(),"success");
        assertEquals(SignUpResult.getError_msg(),null);
    }

    private void testSignUpFail(String username,String password){
        SignUpRequestMessage request = new SignUpRequestMessage();
        request.setPassword(password);
        request.setUsername(username);
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
