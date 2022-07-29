package com.souzaemerson.marvelproject.data.db.repository

import androidx.lifecycle.LiveData
import com.souzaemerson.marvelproject.data.model.Results

interface DatabaseRepository {

    suspend fun insertCharacter(result: Results)
    suspend fun deleteCharacter(result: Results)
    fun getAllCharacters(): LiveData<List<Results>>
    suspend fun getFavoriteCharacter(characterId: Long): Results?
    suspend fun getFavoriteCharacterByUser(characterId: Long, email: String): Results?
    fun getAllCharactersByUser(email: String): LiveData<List<Results>>
}