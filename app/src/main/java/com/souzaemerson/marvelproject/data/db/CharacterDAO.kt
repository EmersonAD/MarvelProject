package com.souzaemerson.marvelproject.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.souzaemerson.marvelproject.data.model.Results

@Dao
interface CharacterDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(result: Results)

    @Query("SELECT * FROM results_table")
    fun getAllCharacters(): LiveData<List<Results>>

    @Delete
    suspend fun deleteCharacter(result: Results)

    @Query("SELECT * FROM results_table WHERE id = :characterId")
    suspend fun getFavoriteCharacter(characterId: Long): Results?
}