package com.souzaemerson.marvelproject.data.db.repository

import androidx.lifecycle.LiveData
import com.souzaemerson.marvelproject.data.db.dao.CharacterDAO
import com.souzaemerson.marvelproject.data.model.Favorites
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(private val dao: CharacterDAO) : DatabaseRepository {

    override suspend fun insertFavorite(result: Favorites) =
        dao.insertCharacter(result)

    override fun getAllCharactersByUser(email: String): LiveData<List<Favorites>> =
        dao.getAllCharactersByUser(email)

    override suspend fun deleteCharacter(result: Favorites) =
        dao.deleteCharacter(result)

    override suspend fun getFavoriteCharacterByUser(characterId: Long, email: String): Favorites? =
        dao.getFavoriteCharacterByUser(characterId, email)
}