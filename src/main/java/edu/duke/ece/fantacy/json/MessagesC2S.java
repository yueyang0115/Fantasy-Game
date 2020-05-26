package edu.duke.ece.fantacy.json;

public class MessagesC2S {
    private LoginRequestMessage loginRequestMessage;
    private SignUpRequestMessage signUpRequestMessage;
    private PositionRequestMessage positionRequestMessage;

    public MessagesC2S(){
        this.loginRequestMessage = new LoginRequestMessage();
        this.signUpRequestMessage = new SignUpRequestMessage();
        this.positionRequestMessage = new PositionRequestMessage();
    }

    public MessagesC2S(LoginRequestMessage loginRequestMessage) {
        this.loginRequestMessage = loginRequestMessage;
    }

    public MessagesC2S(SignUpRequestMessage signUpRequestMessage) {
        this.signUpRequestMessage = signUpRequestMessage;
    }

    public MessagesC2S(PositionRequestMessage positionRequestMessage) {
        this.positionRequestMessage = positionRequestMessage;
    }

    public LoginRequestMessage getLoginRequestMessage() {
        return loginRequestMessage;
    }

    public void setLoginRequestMessage(LoginRequestMessage loginRequestMessage) {
        this.loginRequestMessage = loginRequestMessage;
    }

    public SignUpRequestMessage getSignUpRequestMessage() {
        return signUpRequestMessage;
    }

    public void setSignUpRequestMessage(SignUpRequestMessage signUpRequestMessage) {
        this.signUpRequestMessage = signUpRequestMessage;
    }

    public PositionRequestMessage getPositionRequestMessage() {
        return positionRequestMessage;
    }

    public void setPositionRequestMessage(PositionRequestMessage positionRequestMessage) {
        this.positionRequestMessage = positionRequestMessage;
    }
}
