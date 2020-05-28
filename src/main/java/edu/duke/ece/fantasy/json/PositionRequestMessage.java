package edu.duke.ece.fantasy.json;

public class PositionRequestMessage {
    private int x;
    private int y;

    public PositionRequestMessage(){}

    public PositionRequestMessage(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
