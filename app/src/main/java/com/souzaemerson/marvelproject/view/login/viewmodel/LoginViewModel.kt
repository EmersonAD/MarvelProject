package com.souzaemerson.marvelproject.view.login.viewmodel

import android.util.Patterns
import androidx.lifecycle.*
import com.souzaemerson.marvelproject.R
import com.souzaemerson.marvelproject.core.State
import com.souzaemerson.marvelproject.data.model.User
import com.souzaemerson.marvelproject.data.repository.login.LoginRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: LoginRepository
) : ViewModel() {

    private val _user = MutableLiveData<State<User>>()
    val user: LiveData<State<User>> = _user

    private val _loginFieldErrorResId = MutableLiveData<Int?>()
    val loginFieldErrorResId: LiveData<Int?> = _loginFieldErrorResId

    private val _passwordFieldErrorResId = MutableLiveData<Int?>()
    val passwordFieldErrorResId: LiveData<Int?> = _passwordFieldErrorResId

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private var isValid = false

    fun login(email: String, password: String) =
        viewModelScope.launch {
            isValid = true

            _loginFieldErrorResId.value = getErrorStringResIdEmptyEmail(email)
            _passwordFieldErrorResId.value = getErrorStringResIdEmptyPassword(password)

            if (isValid) {
                _loading.value = true
                try {
                    delay(3000)
                    repository.login(email, password).let { user ->
                        _user.value = State.success(user)
                        _loading.value = false
                    }
                } catch (e: Exception) {
                    _loading.value = false
                    _user.value = State.error(e)
                }
            }
        }

    private fun getErrorStringResIdEmptyEmail(value: String): Int? =
        if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            isValid = false
            R.string.invalid_email
        } else null

    private fun getErrorStringResIdEmptyPassword(value: String): Int? =
        when {
            value.isEmpty() -> {
                isValid = false
                R.string.required_field
            }
            value.length < 8 -> {
                isValid = false
                R.string.small_password
            }
            else -> null
        }

    class LoginViewModelProvideFactory(
        private val repository: LoginRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}