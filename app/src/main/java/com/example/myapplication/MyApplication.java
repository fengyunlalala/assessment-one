package com.example.myapplication;

import android.app.Application;

import androidx.room.Room;

public class MyApplication extends Application {

    public static DatabaseUtil databaseUtil;

    @Override
    public void onCreate() {
        super.onCreate();
        databaseUtil = Room.databaseBuilder(this, DatabaseUtil.class, "30395604.db").build();
    }
}
