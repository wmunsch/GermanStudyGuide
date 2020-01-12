package com.williammunsch.germanstudyguide.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.williammunsch.germanstudyguide.User;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM users_table")
    LiveData<User> getUser();

    @Query("SELECT COUNT(*) FROM  users_table")
    Integer count();

    @Query("DELETE FROM users_table")
    void deleteAll();
}
