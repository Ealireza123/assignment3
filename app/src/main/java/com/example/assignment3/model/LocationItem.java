package com.example.assignment3.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(
        nameInDb = "LocationItem"
)
public class LocationItem {
    @Id(autoincrement = true)
    private long id;

    @Index(unique = true)
    private long parentID;

    @Property(nameInDb = "time")
    private double time;

    @Property(nameInDb = "angle")
    private double angle;

    public LocationItem(long parentID, double time, double angle) {
        this.parentID = parentID;
        this.time = time;
        this.angle = angle;
    }

    @Generated(hash = 576009675)
    public LocationItem(long id, long parentID, double time, double angle) {
        this.id = id;
        this.parentID = parentID;
        this.time = time;
        this.angle = angle;
    }

    @Generated(hash = 18379323)
    public LocationItem() {
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParentID() {
        return this.parentID;
    }

    public void setParentID(long parentID) {
        this.parentID = parentID;
    }
}
