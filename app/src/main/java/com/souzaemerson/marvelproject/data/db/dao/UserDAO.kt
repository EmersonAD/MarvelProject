package com.souzaemerson.marvelproject.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.souzaemerson.marvelproject.data.model.User

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createNewUser(user: User)

    @Query("SELECT * FROM user_table WHERE email = :email AND password = :password")
    suspend fun getValidUser(email: String, password: String): User?

}