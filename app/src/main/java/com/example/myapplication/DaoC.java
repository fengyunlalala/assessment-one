package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoC {

    @Query("select * from BeanC")
    List<BeanC> getList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(BeanC beanC);

    @Delete
    int delete(BeanC beanC);

}
