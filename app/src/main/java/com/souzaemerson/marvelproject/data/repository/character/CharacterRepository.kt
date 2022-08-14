package com.souzaemerson.marvelproject.data.repository.character

import com.souzaemerson.marvelproject.data.model.CharacterResponse

interface CharacterRepository {
    suspend fun getCharacters(
        apikey: String,
        hash: String,
        ts: Long,
        limit: Int,
        offset: Int
    ): CharacterResponse

    suspend fun searchCharacter(
        nameStartsWith: String,
        apikey: String,
        hash: String,
        ts: Long
    ): CharacterResponse
}