package com.souzaemerson.marvelproject.data.repository.character

import com.souzaemerson.marvelproject.data.model.CharacterResponse
import com.souzaemerson.marvelproject.data.network.Service

class CharactersRepositoryImpl(private val api: Service) : CharacterRepository {
    override suspend fun getCharacters(
        apikey: String,
        hash: String,
        ts: Long,
        limit: Int,
        offset: Int
    ): CharacterResponse =
        api.getCharacters(apikey, hash, ts, limit, offset)

    override suspend fun searchCharacter(
        nameStartsWith: String,
        apikey: String,
        hash: String,
        ts: Long
    ): CharacterResponse = api.searchCharacter(nameStartsWith, apikey, hash, ts)

}