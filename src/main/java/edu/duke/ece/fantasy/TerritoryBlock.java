package edu.duke.ece.fantasy;

import java.util.Objects;

public class TerritoryBlock {
    int x;
    int y;
    TerritoryBlock parent;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getType() {
        return type;
    }

    String type;

    public TerritoryBlock(int x, int y, String type, TerritoryBlock parent) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TerritoryBlock)) return false;
        TerritoryBlock that = (TerritoryBlock) o;
        return x == that.x &&
                y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
