package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoT {

    @Query("select * from BeanT")
    List<BeanT> getList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(BeanT beanT);

    @Delete
    int delete(BeanT beanT);

    @Query("select * from BeanT where name = :name and password = :password")
    List<BeanT>getUser(String name, String password);

}
