package com.souzaemerson.marvelproject.data.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.souzaemerson.marvelproject.data.model.Results
import com.souzaemerson.marvelproject.data.model.User

@Dao
interface CharacterDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCharacter(result: Results)

    @Delete
    suspend fun deleteCharacter(result: Results)

    @Query("SELECT * FROM results_table")
    fun getAllCharacters(): LiveData<List<Results>>

    @Query("SELECT * FROM results_table WHERE id = :characterId")
    suspend fun getFavoriteCharacter(characterId: Long): Results?

    @Query("SELECT * FROM results_table WHERE email = :email")
    fun getAllCharactersByUser(email: String): LiveData<List<Results>>

    @Query("SELECT * FROM results_table WHERE id = :characterId AND email = :email")
    suspend fun getFavoriteCharacterByUser(characterId: Long, email: String): Results?
}