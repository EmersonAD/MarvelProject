package com.souzaemerson.marvelproject.view.fragment.detail.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.souzaemerson.marvelproject.R
import com.souzaemerson.marvelproject.core.State
import com.souzaemerson.marvelproject.data.db.AppDatabase
import com.souzaemerson.marvelproject.data.db.CharacterDAO
import com.souzaemerson.marvelproject.data.db.repository.DatabaseRepository
import com.souzaemerson.marvelproject.data.model.Results
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException
import kotlin.coroutines.coroutineContext

class DetailViewModel(
    private val repository: DatabaseRepository,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel() {
    private lateinit var dao: CharacterDAO

    private val _response = MutableLiveData<State<Boolean>>()
    val response: LiveData<State<Boolean>> = _response

    fun insertCharacters(result: Results){
        viewModelScope.launch {
            try {
                 withContext(ioDispatcher){
                     repository.insertCharacter(result)
                 }
                _response.value = State.success(true)
            } catch (throwable: Throwable){
                _response.value = State.error(throwable)
            }
        }
    }

    class DetailViewModelProviderFactory(
        private val repository: DatabaseRepository,
        private val ioDispatcher: CoroutineDispatcher
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)){
                return DetailViewModel(repository, ioDispatcher) as T
            }
            throw IllegalArgumentException("Unknown viewModel Class")
        }
    }
}