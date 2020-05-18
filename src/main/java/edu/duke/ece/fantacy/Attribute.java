package edu.duke.ece.fantacy;

public class Attribute {
    private Location location;

    public Attribute(){
        this.location = new Location();
    }

    public void setLocation(Location location_rhs){
        location.setX(location_rhs.getX());
        location.setY(location_rhs.getY());
    }

    public Location getLocation(){ return this.location; }
}
