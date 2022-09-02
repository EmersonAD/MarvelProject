package com.souzaemerson.marvelproject.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.souzaemerson.marvelproject.data.model.Favorites

@Dao
interface CharacterDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCharacter(result: Favorites)

    @Delete
    suspend fun deleteCharacter(result: Favorites)

    @Query("SELECT * FROM favorites_table WHERE email = :email")
    fun getAllCharactersByUser(email: String): LiveData<List<Favorites>>

    @Query("SELECT * FROM favorites_table WHERE id = :characterId AND email = :email")
    suspend fun getFavoriteCharacterByUser(characterId: Long, email: String): Favorites?
}