package com.example.myapplication;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {BeanS.class, BeanC.class, BeanB.class, BeanT.class}, version = 1, exportSchema = false)
public abstract class DatabaseUtil extends RoomDatabase {

    public abstract DaoS msd();

    public abstract DaoC mcd();

    public abstract DaoB mbd();

    public abstract DaoT mtd();

}
