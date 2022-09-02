package com.souzaemerson.marvelproject.view.detail.viewmodel

import androidx.lifecycle.*
import com.souzaemerson.marvelproject.core.State
import com.souzaemerson.marvelproject.data.db.repository.DatabaseRepository
import com.souzaemerson.marvelproject.data.model.Favorites
import com.souzaemerson.marvelproject.data.model.comic.ComicResponse
import com.souzaemerson.marvelproject.data.repository.category.CategoryRepository
import com.souzaemerson.marvelproject.di.qualifier.IO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    @IO private val ioDispatcher: CoroutineDispatcher,
    private var categoryRepository: CategoryRepository
) : ViewModel() {

    private val _response = MutableLiveData<State<Boolean>>()
    val response: LiveData<State<Boolean>> = _response

    private val _comicsResponse = MutableLiveData<State<ComicResponse>>()
    val comicsResponse: LiveData<State<ComicResponse>> = _comicsResponse

    private val _seriesResponse = MutableLiveData<State<ComicResponse>>()
    val seriesResponse: LiveData<State<ComicResponse>> = _seriesResponse

    private val _verifyCharacter = MutableLiveData<State<Boolean>>()
    val verifyCharacter: LiveData<State<Boolean>> = _verifyCharacter

    private val _delete = MutableLiveData<State<Boolean>>()
    val delete: LiveData<State<Boolean>>
        get() = _delete


    fun getCategory(apikey: String, hash: String, ts: Long, id: Long) {
        viewModelScope.launch {
            try {
                _comicsResponse.value = State.loading(true)
                withContext(ioDispatcher) {
                    categoryRepository.getComics(apikey, hash, ts, id)
                }.let {
                    _comicsResponse.value = State.loading(false)
                    _comicsResponse.value = State.success(it)
                }
            } catch (e: Exception) {
                Timber.tag("Error").e(e)
                _comicsResponse.value = State.error(e)
            }
        }
    }

    fun getSeries(apikey: String, hash: String, ts: Long, id: Long) {
        viewModelScope.launch {
            try {
                _seriesResponse.value = State.loading(true)
                withContext(ioDispatcher) {
                    categoryRepository.getSeries(apikey, hash, ts, id)
                }.let {
                    _seriesResponse.value = State.loading(false)
                    _seriesResponse.value = State.success(it)
                }
            } catch (e: Exception) {
                Timber.tag("Error").e(e)
                _seriesResponse.value = State.error(e)
            }
        }
    }

    fun insertFavorite(favorite: Favorites) {
        viewModelScope.launch {
            try {
                databaseRepository.insertFavorite(favorite)
            } catch (e: Exception) {
                //Timber
            }
        }
    }

    fun verifySavedCharacter(characterId: Long, email: String) {
        viewModelScope.launch {
            try {
                val result = withContext(ioDispatcher) {
                    databaseRepository.getFavoriteCharacterByUser(characterId, email)
                }
                _verifyCharacter.value = State.success(result != null)

            } catch (throwable: Throwable) {
                _verifyCharacter.value = State.error(throwable)
            }
        }
    }

    fun deleteCharacter(favorite: Favorites) = viewModelScope.launch {
        try {
            _delete.value = State.loading(true)
            withContext(ioDispatcher) {
                databaseRepository.deleteCharacter(favorite)
            }
            _delete.value = State.loading(false)
            _delete.value = State.success(true)
        } catch (e: Exception) {
            _delete.value = State.loading(false)
            _delete.value = State.error(e)
        }
    }
}