package com.souzaemerson.marvelproject.data.db.repository

import androidx.lifecycle.LiveData
import com.souzaemerson.marvelproject.data.model.Results

interface DatabaseRepository {

    suspend fun insertCharacter(result: Results)
    fun getAllCharacters(): LiveData<List<Results>>
    suspend fun deleteCharacter(result: Results)
}