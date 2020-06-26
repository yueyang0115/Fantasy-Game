package edu.duke.ece.fantasy.building;

public class InvalidBuildingRequest extends Exception {
    public InvalidBuildingRequest(String mesg){
        super(mesg);
    }
}
