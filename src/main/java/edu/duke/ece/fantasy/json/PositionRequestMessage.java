package edu.duke.ece.fantasy.json;

public class PositionRequestMessage {
    private double x;
    private double y;

    public PositionRequestMessage(){}

    public PositionRequestMessage(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
