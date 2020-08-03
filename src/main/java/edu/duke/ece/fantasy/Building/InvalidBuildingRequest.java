package edu.duke.ece.fantasy.Building;

public class InvalidBuildingRequest extends Exception {
    public InvalidBuildingRequest(String mesg){
        super(mesg);
    }
}
