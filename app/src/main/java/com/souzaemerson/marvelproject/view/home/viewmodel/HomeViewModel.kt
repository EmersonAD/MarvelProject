package com.souzaemerson.marvelproject.view.home.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.souzaemerson.marvelproject.core.State
import com.souzaemerson.marvelproject.core.preferences.PreferencesUtil
import com.souzaemerson.marvelproject.data.model.CharacterResponse
import com.souzaemerson.marvelproject.data.model.Results
import com.souzaemerson.marvelproject.data.repository.character.CharacterRepository
import com.souzaemerson.marvelproject.di.qualifier.IO
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext application: Context,
    private val repository: CharacterRepository,
    @IO private val ioDispatcher: CoroutineDispatcher
) : AndroidViewModel(application as Application) {

    private val _response = MutableLiveData<State<CharacterResponse>>()
    val response: LiveData<State<CharacterResponse>> = _response

    private val _responseInCache = MutableLiveData<List<Results>>()
    val responseInCache: LiveData<List<Results>> get() = _responseInCache

    private val _search = MutableLiveData<State<CharacterResponse>>()
    val search: LiveData<State<CharacterResponse>> = _search

    fun getCharacters(apikey: String, hash: String, ts: Long, limit: Int, offset: Int) {
        viewModelScope.launch {
            if (!existsInCache()) {
                try {
                    _response.value = State.loading(true)

                    withContext(ioDispatcher) {
                        repository.getCharacters(apikey, hash, ts, limit, offset)
                    }.let { characterResponse ->
                        _response.value = State.loading(false)
                        _response.value = State.success(characterResponse)
                        populateCharacter(characterResponse)
                    }
                } catch (throwable: Throwable) {
                    _response.value = State.error(throwable)
                    _response.value = State.loading(false)
                }
            } else {
                getCharacterReponseInCache()?.let { characterResponse ->
                    _responseInCache.value = characterResponse.data.results
                }
            }
        }
    }

    fun searchCharacter(nameStartsWith: String, apikey: String, hash: String, ts: Long) {
        viewModelScope.launch {
            try {
                _search.value = State.loading(true)
                val searchResponse = withContext(ioDispatcher) {
                    repository.searchCharacter(nameStartsWith, apikey, hash, ts)
                }
                _search.value = State.success(searchResponse)
                _search.value = State.loading(false)
            } catch (throwable: Throwable) {
                _search.value = State.error(throwable)
                _search.value = State.loading(false)
            }
        }
    }

    private fun existsInCache(): Boolean =
        PreferencesUtil(getApplication()).containsObjectInPreferences("CHARACTER")

    private fun populateCharacter(characterResponse: CharacterResponse) {
        PreferencesUtil(getApplication()).putObjectIntoPreferences("CHARACTER", characterResponse)
    }

    private fun getCharacterReponseInCache() =
        PreferencesUtil(getApplication()).getObjectFromPreferences(
            "CHARACTER",
            CharacterResponse::class.java
        )
}