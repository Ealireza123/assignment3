package com.example.assignment3.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

@Entity(
        nameInDb = "LocationItem"
)
public class LocationItem {
    @Id(autoincrement = true)
    private Long _id;

    @Property(nameInDb = "parentID")
    private Long parentID;

    @Property(nameInDb = "time")
    private double time;

    @Property(nameInDb = "angle")
    private double angle;

    public LocationItem(double time, double angle) {
        this.time = time;
        this.angle = angle;
    }

    @Generated(hash = 1669079339)
    public LocationItem(Long _id, Long parentID, double time, double angle) {
        this._id = _id;
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

    public Long getParentID() {
        return this.parentID;
    }

    public void setParentID(Long parentID) {
        this.parentID = parentID;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }
}
