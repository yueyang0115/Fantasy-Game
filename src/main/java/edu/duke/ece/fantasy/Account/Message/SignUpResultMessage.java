package edu.duke.ece.fantasy.Account.Message;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.duke.ece.fantasy.Account.CmdAccount;
import edu.duke.ece.fantasy.net.Message;
import edu.duke.ece.fantasy.net.MessageMeta;
import edu.duke.ece.fantasy.net.Modules;

@JsonInclude(JsonInclude.Include.NON_NULL)
@MessageMeta(module = Modules.ACCOUNT, cmd = CmdAccount.RES_SIGNUP)
public class SignUpResultMessage extends Message {
    private String error_msg;
    private String status;

    public SignUpResultMessage() {}

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
