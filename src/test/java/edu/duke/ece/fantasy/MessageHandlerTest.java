package edu.duke.ece.fantasy;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.Friend.Message.FriendRequestMessage;
import org.junit.jupiter.api.Test;

public class MessageHandlerTest {
//    MessageHandler mh = new MessageHandler();
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testAll(){
        FriendRequestMessage msg = new FriendRequestMessage();
        msg.setAction(FriendRequestMessage.ActionType.search);
        try{
            String tmp = objectMapper.writeValueAsString(msg);
//            System.out.println(tmp);
            FriendRequestMessage res = objectMapper.readValue(tmp,FriendRequestMessage.class);
            tmp = objectMapper.writeValueAsString(res);
            System.out.println(tmp);
        } catch (Exception e){
            e.printStackTrace();
        }
        //testLogin();
        //testSignUp();
    }

//    @Test
//    void testWid(){
//        MessageHandler mh = new MessageHandler();
//        LoginRequestMessage loginRequest = new LoginRequestMessage();
//        loginRequest.setUsername("4321");
//        loginRequest.setPassword("4321");
//        MessagesC2S request = new MessagesC2S(loginRequest);
//        MessagesS2C result = mh.handle(request);
//    }
//
//    void testLogin(){
//        MessageHandler mh = new MessageHandler();
//        LoginRequestMessage loginRequest = new LoginRequestMessage();
//        loginRequest.setUsername("00");
//        loginRequest.setPassword("11");
//        MessagesC2S request = new MessagesC2S(loginRequest);
//        MessagesS2C result = mh.handle(request);
//        assertEquals(result.getSignUpResultMessage(),null);
//        assertEquals(result.getPositionResultMessage(),null);
//        assertEquals(result.getLoginResultMessage().getStatus(),"fail");
//        assertNotNull(result.getLoginResultMessage().getError_msg());
//    }
//
//    void testSignUp(){
//        MessageHandler mh = new MessageHandler();
//        SignUpRequestMessage signUpRequest = new SignUpRequestMessage();
//        signUpRequest.setUsername("00");
//        signUpRequest.setPassword("11");
//        MessagesC2S request = new MessagesC2S(signUpRequest);
//        MessagesS2C result = mh.handle(request);
//        assertEquals(result.getLoginResultMessage(),null);
//        assertEquals(result.getPositionResultMessage(),null);
//        assertEquals(result.getSignUpResultMessage().getStatus(),"success");
//        assertEquals(result.getSignUpResultMessage().getError_msg(),null);
//    }
//
//    @Test
//    void testPositionUpdate(){
//        (new Initializer()).initialize();
//        PositionRequestMessage positionRequestMessage = new PositionRequestMessage();
//        positionRequestMessage.setX(-5);
//        positionRequestMessage.setY(15);
//        MessagesC2S request = new MessagesC2S(positionRequestMessage);
//        MessagesS2C res = mh.handle(request);
//        try{
//            String tmp = objectMapper.writeValueAsString(res);
//            System.out.println(tmp);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
