package com.example.assignment3.model;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.ArrayList;
import java.util.List;

@Entity(
        nameInDb = "TimeBaseDataModel"
)
public class TimeBaseDataModel {
    @Id(autoincrement = true)
    private Long _id;

    @Property(nameInDb = "testTime")
    private String testTime;

    @Property(nameInDb = "locationItemList")
    @ToMany(referencedJoinProperty = "parentID")
    private List<LocationItem> locationItemList;

    public void addItemToList(long timestamp, double angle, DataType dataType){
        if(locationItemList == null || locationItemList.isEmpty()) {
            locationItemList = new ArrayList<>();
            LocationItem tmp = new LocationItem(timestamp, angle, dataType);
            locationItemList.add(tmp);
        }else {
            LocationItem lastLocationItem =
                    locationItemList.get(locationItemList.size() - 1);
            if(lastLocationItem.getTime() == timestamp) {
                lastLocationItem.addSecondAngle(angle, dataType);
            }else{
                LocationItem tmp = new LocationItem(timestamp, angle, dataType);
                locationItemList.add(tmp);
            }
        }
    }

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1510825113)
    private transient TimeBaseDataModelDao myDao;

    @Generated(hash = 1805715807)
    public TimeBaseDataModel(Long _id, String testTime) {
        this._id = _id;
        this.testTime = testTime;
    }

    @Generated(hash = 704940713)
    public TimeBaseDataModel() {
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 143222265)
    public List<LocationItem> getLocationItemList() {
        if (locationItemList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LocationItemDao targetDao = daoSession.getLocationItemDao();
            List<LocationItem> locationItemListNew = targetDao
                    ._queryTimeBaseDataModel_LocationItemList(_id);
            synchronized (this) {
                if (locationItemList == null) {
                    locationItemList = locationItemListNew;
                }
            }
        }
        return locationItemList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 918375788)
    public synchronized void resetLocationItemList() {
        locationItemList = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1617742316)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTimeBaseDataModelDao() : null;
    }

}
