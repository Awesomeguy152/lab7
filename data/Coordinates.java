package org.example.data;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private long x;
    private long y;

    public Coordinates(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public String toXML() {
        return "<coordinates>\n" +
                "  <x>" + x + "</x>\n" +
                "  <y>" + y + "</y>\n" +
                "</coordinates>";
    }
}
