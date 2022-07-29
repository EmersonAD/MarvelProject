package com.souzaemerson.marvelproject.data.db.repository

import androidx.lifecycle.LiveData
import com.souzaemerson.marvelproject.data.db.daos.CharacterDAO
import com.souzaemerson.marvelproject.data.model.Results

class DatabaseRepositoryImpl(private val dao: CharacterDAO) : DatabaseRepository {
    override suspend fun insertCharacter(result: Results) =
        dao.insertCharacter(result)

    override fun getAllCharacters(): LiveData<List<Results>> =
        dao.getAllCharacters()

    override fun getAllCharactersByUser(email: String): LiveData<List<Results>> =
        dao.getAllCharactersByUser(email)

    override suspend fun deleteCharacter(result: Results) =
        dao.deleteCharacter(result)

    override suspend fun getFavoriteCharacter(characterId: Long): Results? =
        dao.getFavoriteCharacter(characterId)

    override suspend fun getFavoriteCharacterByUser(characterId: Long, email: String): Results? =
        dao.getFavoriteCharacterByUser(characterId, email)

}