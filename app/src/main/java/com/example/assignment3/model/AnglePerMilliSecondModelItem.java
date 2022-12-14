package com.example.assignment3.model;

public class AnglePerMilliSecondModelItem {
    private final long time;
    private final double angle;

    public AnglePerMilliSecondModelItem(long time, double angle) {
        this.time = time;
       this.angle = angle;
    }

    public long getTime() {
        return time;
    }
    public double getAngle() {
        return angle;
    }
}
