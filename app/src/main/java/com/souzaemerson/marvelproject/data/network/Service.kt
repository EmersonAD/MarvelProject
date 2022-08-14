package com.souzaemerson.marvelproject.data.network

import com.souzaemerson.marvelproject.data.model.CharacterResponse
import com.souzaemerson.marvelproject.data.model.comic.ComicResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Service {

    @GET("/v1/public/characters")
    suspend fun getCharacters(
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: Long,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): CharacterResponse

    @GET("v1/public/characters/{id}/comics")
    suspend fun getComics(
        @Path("id") id: Long,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: Long
    ): ComicResponse

    @GET("v1/public/characters/{id}/series")
    suspend fun getSeries(
        @Path("id") id: Long,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: Long
    ): ComicResponse

    @GET("/v1/public/characters")
    suspend fun searchCharacter(
        @Query("nameStartsWith") nameStartsWith: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: Long
    ): CharacterResponse

}