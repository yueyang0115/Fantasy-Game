package edu.duke.ece.fantasy.Account.Message;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.duke.ece.fantasy.Account.CmdAccount;
import edu.duke.ece.fantasy.net.Message;
import edu.duke.ece.fantasy.net.MessageMeta;
import edu.duke.ece.fantasy.net.Modules;

@JsonInclude(JsonInclude.Include.NON_NULL)
@MessageMeta(module = Modules.ACCOUNT, cmd = CmdAccount.REQ_LOGIN)
public class LoginRequestMessage extends Message {
    private String username;
    private String password;

    public LoginRequestMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginRequestMessage() {
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
