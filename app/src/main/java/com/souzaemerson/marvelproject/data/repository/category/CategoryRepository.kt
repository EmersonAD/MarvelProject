package com.souzaemerson.marvelproject.data.repository.category

import com.souzaemerson.marvelproject.data.model.comic.ComicResponse

interface CategoryRepository {
    suspend fun getComics(
        apikey: String,
        hash: String,
        ts: Long,
        id: Long
    ): ComicResponse

    suspend fun getSeries(
        apikey: String,
        hash: String,
        ts: Long,
        id: Long
    ): ComicResponse
}