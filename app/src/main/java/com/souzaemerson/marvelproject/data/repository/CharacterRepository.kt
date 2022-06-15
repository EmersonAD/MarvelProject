package com.souzaemerson.marvelproject.data.repository

import com.souzaemerson.marvelproject.data.model.CharacterResponse

interface CharacterRepository {
    suspend fun getCharacters(apikey: String, hash: String, ts: Long): CharacterResponse
}