package edu.duke.ece.fantacy.json;

import java.util.List;

public class MessagesS2C {
    private PositionResultMessage positionResultMessage;
    private LoginResultMessage loginResultMessage;
    private SignUpResultMessage signUpResultMessage;

    public MessagesS2C(){
        this.positionResultMessage = new PositionResultMessage();
        this.loginResultMessage = new LoginResultMessage();
        this.signUpResultMessage = new SignUpResultMessage();
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
}
