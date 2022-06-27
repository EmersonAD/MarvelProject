package com.souzaemerson.marvelproject.view.fragment.favorite.viewmodel

import androidx.lifecycle.ViewModel
import com.souzaemerson.marvelproject.data.db.repository.DatabaseRepository
import kotlinx.coroutines.CoroutineDispatcher

class FavoriteViewModel(
    private val repository: DatabaseRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    fun getCharacters() =
        repository.getAllCharacters()

}