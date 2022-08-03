package com.souzaemerson.marvelproject.data.db.repository

import androidx.lifecycle.LiveData
import com.souzaemerson.marvelproject.data.model.Favorites
import com.souzaemerson.marvelproject.data.model.Results

interface DatabaseRepository {

    suspend fun insertCharacter(result: Favorites)
    suspend fun insertFavorite(result: Favorites)
    suspend fun deleteCharacter(result: Favorites)
    fun getAllCharacters(): LiveData<List<Favorites>>
    suspend fun getFavoriteCharacter(characterId: Long): Favorites?
    suspend fun getFavoriteCharacterByUser(characterId: Long, email: String): Favorites?
    fun getAllCharactersByUser(email: String): LiveData<List<Favorites>>
}