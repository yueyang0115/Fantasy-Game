package edu.duke.ece.fantasy.json;

public class MessagesS2C {
    private PositionResultMessage positionResultMessage;
    private LoginResultMessage loginResultMessage;
    private SignUpResultMessage signUpResultMessage;
    private BattleResultMessage battleResultMessage;

    public MessagesS2C(){ }

    public MessagesS2C(LoginResultMessage msg){
        this.loginResultMessage = msg;
    }

   public MessagesS2C(SignUpResultMessage msg){
        this.signUpResultMessage = msg;
   }

   public MessagesS2C(PositionResultMessage msg){
        this.positionResultMessage = msg;
   }

    public MessagesS2C(BattleResultMessage msg){
        this.battleResultMessage = msg;
    }

    public PositionResultMessage getPositionResultMessage() {
        return positionResultMessage;
    }

    public void setPositionResultMessage(PositionResultMessage positionResultMessage) {
        this.positionResultMessage = positionResultMessage;
    }

    public LoginResultMessage getLoginResultMessage() {
        return loginResultMessage;
    }

    public void setLoginResultMessage(LoginResultMessage loginResultMessage) {
        this.loginResultMessage = loginResultMessage;
    }

    public SignUpResultMessage getSignUpResultMessage() {
        return signUpResultMessage;
    }

    public void setSignUpResultMessage(SignUpResultMessage signUpResultMessage) {
        this.signUpResultMessage = signUpResultMessage;
    }

    public BattleResultMessage getBattleResultMessage() {
        return battleResultMessage;
    }

    public void setBattleResultMessage(BattleResultMessage battleResultMessage) {
        this.battleResultMessage = battleResultMessage;
    }
}
