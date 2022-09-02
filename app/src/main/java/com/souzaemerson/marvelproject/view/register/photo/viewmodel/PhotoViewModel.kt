package com.souzaemerson.marvelproject.view.register.photo.viewmodel

import androidx.lifecycle.*
import com.souzaemerson.marvelproject.core.State
import com.souzaemerson.marvelproject.data.model.User
import com.souzaemerson.marvelproject.data.repository.register.RegisterRepository
import com.souzaemerson.marvelproject.di.qualifier.IO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val repository: RegisterRepository,
    @IO private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _user = MutableLiveData<State<Boolean>>()
    val user: LiveData<State<Boolean>> = _user

    fun insertNewUserOnDatabase(user: User) {
        viewModelScope.launch {
            try {
                _user.value = State.loading(true)
                withContext(ioDispatcher) {
                    repository.registerNewUser(user)
                }
                _user.value = State.loading(false)
                _user.value = State.success(true)
            } catch (e: Exception) {
                _user.value = State.error(e)
            }
        }
    }
}