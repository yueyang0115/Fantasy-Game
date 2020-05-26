package edu.duke.ece.fantacy.json;

public class SignUpResultMessage {
    private String error_msg;
    private String status;

    public SignUpResultMessage() {
    }

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
