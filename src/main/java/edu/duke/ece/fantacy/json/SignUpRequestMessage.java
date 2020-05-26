package edu.duke.ece.fantacy.json;

public class SignUpRequestMessage {
    private String username;
    private String password;

    public SignUpRequestMessage(){}

    public SignUpRequestMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
