package com.souzaemerson.marvelproject.data.repository

import com.souzaemerson.marvelproject.data.model.CharacterResponse


class CharactersRepositoryMock() : CharacterRepository {
    override suspend fun getCharacters(apikey: String, hash: String, ts: Long): CharacterResponse {
        TODO("Not yet implemented")
    }
}