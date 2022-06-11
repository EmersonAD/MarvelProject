package com.souzaemerson.marvelproject.data.network

import com.souzaemerson.marvelproject.data.model.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("/v1/public/characters")
    suspend fun getCharacters(
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: Long
    ): CharacterResponse
}