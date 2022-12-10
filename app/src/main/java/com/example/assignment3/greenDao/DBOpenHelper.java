package com.example.assignment3.greenDao;

import android.content.Context;

import com.example.assignment3.model.DaoMaster;


public class DBOpenHelper extends DaoMaster.OpenHelper {
    private static final String NAME = "angel.db";

    public DBOpenHelper(Context context) {
        super(context, NAME);
    }
}
