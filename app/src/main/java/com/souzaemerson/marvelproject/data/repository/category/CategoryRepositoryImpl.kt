package com.souzaemerson.marvelproject.data.repository.category

import com.souzaemerson.marvelproject.data.model.comic.ComicResponse
import com.souzaemerson.marvelproject.data.network.Service
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(private val api: Service) : CategoryRepository {
    override suspend fun getComics(
        apikey: String,
        hash: String,
        ts: Long,
        id: Long
    ): ComicResponse =
        api.getComics(apikey = apikey, hash = hash, ts = ts, id = id)

    override suspend fun getSeries(
        apikey: String,
        hash: String,
        ts: Long,
        id: Long
    ): ComicResponse =
        api.getSeries(id = id, apikey = apikey, hash = hash, ts = ts)

}