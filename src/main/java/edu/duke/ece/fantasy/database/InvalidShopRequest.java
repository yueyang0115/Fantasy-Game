package edu.duke.ece.fantasy.database;

public class InvalidShopRequest extends Exception {
    public InvalidShopRequest(String mesg){
        super(mesg);
    }
}
