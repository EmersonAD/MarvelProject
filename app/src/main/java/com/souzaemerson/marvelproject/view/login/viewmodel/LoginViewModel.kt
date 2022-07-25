package com.souzaemerson.marvelproject.view.login.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souzaemerson.marvelproject.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

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
                delay(3000)
                _loading.value = false
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
}