package com.example.assignment3.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.assignment3.greenDao.DBOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Repository {
    private static Repository instance;
    private Context context;
    private TimeBaseDataModelDao timeBaseDataModelDao;
    private LocationItemDao locationItemDao;

    private Repository(Context context) {
        this.context = context;

        SQLiteDatabase db = new DBOpenHelper(context).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();

        timeBaseDataModelDao = daoSession.getTimeBaseDataModelDao();
        locationItemDao = daoSession.getLocationItemDao();
    }

    private Repository() {
    }

    //singleton design pattern
    public static Repository getInstance(Context context) {
        if (instance == null) {
            instance = new Repository(context);
        }
        return instance;
    }

    public List<TimeBaseDataModel> getSearchedList() {
        return timeBaseDataModelDao
                .queryBuilder()
                .where(LocationItemDao
                        .Properties
                        .Id
                        .eq(TimeBaseDataModelDao
                                .Properties.Id))
                .list();
    }


    public void saveLastSearched(TimeBaseDataModel newData) {
        SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd | HH:mm:ss",
                Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date().toString());
        newData.setTestTime(currentDateAndTime);

        long parentRowID = timeBaseDataModelDao.insert(newData);
        for (LocationItem locationItem : newData.getLocationItemList()) {
            locationItem.setParentID(parentRowID);
            locationItemDao.insert(locationItem);
        }
    }


}
