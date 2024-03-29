package com.souzaemerson.marvelproject.view.home.viewmodel

import androidx.lifecycle.*
import com.souzaemerson.marvelproject.core.State
import com.souzaemerson.marvelproject.data.model.CharacterResponse
import com.souzaemerson.marvelproject.data.repository.character.CharacterRepository
import com.souzaemerson.marvelproject.di.qualifier.IO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CharacterRepository,
    @IO private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _response = MutableLiveData<State<CharacterResponse>>()
    val response: LiveData<State<CharacterResponse>> = _response

    private val _search = MutableLiveData<State<CharacterResponse>>()
    val search: LiveData<State<CharacterResponse>> = _search

    fun getCharacters(apikey: String, hash: String, ts: Long, limit: Int, offset: Int) {
        viewModelScope.launch {
            try {
                _response.value = State.loading(true)

                val response = withContext(ioDispatcher) {
                    repository.getCharacters(apikey, hash, ts, limit, offset)
                }
                _response.value = State.loading(false)
                _response.value = State.success(response)
            } catch (throwable: Throwable) {
                _response.value = State.error(throwable)
                _response.value = State.loading(false)
            }
        }
    }

    fun searchCharacter(nameStartsWith: String, apikey: String, hash: String, ts: Long){
        viewModelScope.launch {
            try {
                _search.value = State.loading(true)
                val searchResponse = withContext(ioDispatcher){
                    repository.searchCharacter(nameStartsWith, apikey, hash, ts)
                }
                _search.value = State.success(searchResponse)
                _search.value = State.loading(false)
            } catch (throwable: Throwable){
                _search.value = State.error(throwable)
                _search.value = State.loading(false)
            }
        }
    }
}