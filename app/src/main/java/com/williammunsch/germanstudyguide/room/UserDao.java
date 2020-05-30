package com.williammunsch.germanstudyguide.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.williammunsch.germanstudyguide.User;

/**
 * The data access object responsible for retrieving user accounts from the local ROOM database.
 * ROOM creates the DAO at compile time.
 */

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
