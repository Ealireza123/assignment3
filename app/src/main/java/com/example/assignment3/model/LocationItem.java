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

    @Property(nameInDb = "firstMethodAngle")
    private double firstMethodAngle;

    @Property(nameInDb = "secondMethodAngle")
    private double secondMethodAngle;

    public LocationItem(double time, double angle, DataType dataType) {
        this.time = time;
        if(dataType == DataType.FIRST_METHOD_ANGLE){
            this.firstMethodAngle = angle;
        }else if(dataType == DataType.SECOND_METHOD_ANGLE) {
            this.secondMethodAngle = angle;
        }
    }

    @Generated(hash = 1398719274)
    public LocationItem(Long _id, Long parentID, double time, double firstMethodAngle,
            double secondMethodAngle) {
        this._id = _id;
        this.parentID = parentID;
        this.time = time;
        this.firstMethodAngle = firstMethodAngle;
        this.secondMethodAngle = secondMethodAngle;
    }

    public void addSecondAngle(double angle, DataType dataType){
        if(dataType == DataType.FIRST_METHOD_ANGLE){
            firstMethodAngle = angle;
        }else if(dataType == DataType.SECOND_METHOD_ANGLE) {
            secondMethodAngle = angle;
        }
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

    public double getFirstMethodAngle() {
        return firstMethodAngle;
    }

    public void setFirstMethodAngle(double firstMethodAngle) {
        this.firstMethodAngle = firstMethodAngle;
    }

    public double getSecondMethodAngle() {
        return secondMethodAngle;
    }

    public void setSecondMethodAngle(double secondMethodAngle) {
        this.secondMethodAngle = secondMethodAngle;
    }
}
