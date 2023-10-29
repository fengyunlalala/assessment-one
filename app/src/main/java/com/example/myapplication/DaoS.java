package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoS {

    @Query("select * from BeanS")
    List<BeanS> getList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(BeanS beanS);

    @Delete
    int delete(BeanS beanS);

}
