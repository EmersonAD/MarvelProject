package com.souzaemerson.marvelproject.data.db.repository

import androidx.lifecycle.LiveData
import com.souzaemerson.marvelproject.data.db.daos.CharacterDAO
import com.souzaemerson.marvelproject.data.model.Favorites
import com.souzaemerson.marvelproject.data.model.Results

class DatabaseRepositoryImpl(private val dao: CharacterDAO) : DatabaseRepository {
    override suspend fun insertCharacter(result: Favorites) =
        dao.insertCharacter(result)

    override suspend fun insertFavorite(result: Favorites) {
        dao.insertCharacter(result)
    }

    override fun getAllCharacters(): LiveData<List<Favorites>> =
        dao.getAllCharacters()

    override fun getAllCharactersByUser(email: String): LiveData<List<Favorites>> =
        dao.getAllCharactersByUser(email)

    override suspend fun deleteCharacter(result: Favorites) =
        dao.deleteCharacter(result)

    override suspend fun getFavoriteCharacter(characterId: Long): Favorites? =
        dao.getFavoriteCharacter(characterId)

    override suspend fun getFavoriteCharacterByUser(characterId: Long, email: String): Favorites? =
        dao.getFavoriteCharacterByUser(characterId, email)

}