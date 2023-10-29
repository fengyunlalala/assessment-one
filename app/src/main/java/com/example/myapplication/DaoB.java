package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoB {

    @Query("select * from BeanB")
    List<BeanB> getList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(BeanB beanB);

    @Delete
    int delete(BeanB beanB);

    @Query("delete from BeanB where cid = :clazz and time = :time")
    void deleteDate (String clazz, String time);

    @Query("select * from BeanB where cid = :clazz and time = :time")
    List<BeanB> getBindList(String clazz, String time);

}
