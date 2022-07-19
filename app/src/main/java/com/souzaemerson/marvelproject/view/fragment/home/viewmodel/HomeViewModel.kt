package com.souzaemerson.marvelproject.view.fragment.home.viewmodel

import androidx.lifecycle.*
import com.souzaemerson.marvelproject.core.State
import com.souzaemerson.marvelproject.data.model.CharacterResponse
import com.souzaemerson.marvelproject.data.repository.CharacterRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val repository: CharacterRepository,
    private val ioDispatcher: CoroutineDispatcher
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

    class HomeViewModelProviderFactory(
        private val repository: CharacterRepository,
        private val ioDispatcher: CoroutineDispatcher
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(repository, ioDispatcher) as T
            }
            throw IllegalArgumentException("Unknown viewModel Class")
        }
    }
}