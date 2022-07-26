package com.souzaemerson.marvelproject.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.souzaemerson.marvelproject.data.model.Results
import com.souzaemerson.marvelproject.data.model.User

@Dao
interface CharacterDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(result: Results)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createNewUser(user: User)

    @Query("SELECT * FROM user_table WHERE email = :email AND password = :password")
    suspend fun getValidUser(email: String, password: String): User?

    @Query("SELECT * FROM results_table")
    fun getAllCharacters(): LiveData<List<Results>>

    @Delete
    suspend fun deleteCharacter(result: Results)

    @Query("SELECT * FROM results_table WHERE id = :characterId")
    suspend fun getFavoriteCharacter(characterId: Long): Results?
}