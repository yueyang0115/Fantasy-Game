package edu.duke.ece.fantacy;

import edu.duke.ece.fantacy.json.*;

public class MessageHandler {
    private DBprocessor myDBprocessor;
    private int wid;

    public MessageHandler(DBprocessor dbp, int id){
        this.myDBprocessor = dbp;
        this.wid = id;
    }

    public MessagesS2C handle(MessagesC2S input){
        MessagesS2C result = new MessagesS2C();
        LoginRequestMessage loginMsg = input.getLoginRequestMessage();
        SignUpRequestMessage signupMsg = input.getSignUpRequestMessage();
        PositionRequestMessage positionMsg = input.getPositionRequestMessage();

        if(loginMsg != null){
            LoginHandler lh = new LoginHandler(myDBprocessor);
            result.setLoginResultMessage(lh.handle(loginMsg));
            wid = result.getLoginResultMessage().getWid();
        }

        else if(signupMsg != null){
            SignUpHandler sh = new SignUpHandler(myDBprocessor, wid);
            result.setSignUpResultMessage(sh.handle(signupMsg));
        }

        else if(positionMsg != null){
            TerritoryHandler th = new TerritoryHandler();
            PositionResultMessage positionResultMessage = new PositionResultMessage();
            th.addTerritories(wid, positionMsg.getX(),positionMsg.getY());
            positionResultMessage.setTerritory_array(th.getTerritories(wid, positionMsg.getX(),positionMsg.getY()));
            result.setPositionResultMessage(positionResultMessage);
        }
        else{

        }
        return result;
    }

    public int getWid(){ return this.wid;}
}
