package com.souzaemerson.marvelproject.view.register.account.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.souzaemerson.marvelproject.R
import dagger.hilt.android.lifecycle.HiltViewModel

class AccountViewModel : ViewModel() {

    private val _emailFieldErrorResId = MutableLiveData<Int?>()
    val emailFieldErrorResId: LiveData<Int?> = _emailFieldErrorResId

    private val _nameFieldErrorResId = MutableLiveData<Int?>()
    val nameFieldErrorResId: LiveData<Int?> = _nameFieldErrorResId

    private val _sameEmailsFieldErrorResId = MutableLiveData<Int?>()
    val sameEmailsFieldErrorResId: LiveData<Int?> = _sameEmailsFieldErrorResId

    fun checkIfAllFieldAreValid(email: String, emailConfirmation: String, name: String): Boolean {

        _nameFieldErrorResId.value = getErrorStringResIdInvalidName(name)
        _emailFieldErrorResId.value = getErrorStringResIdEmptyEmail(email)

        return if (emailConfirmation == email && name.isNotEmpty()) {
            true
        } else {
            _sameEmailsFieldErrorResId.value =
                getErrorStringResIdEmailIsNotTheSame(email, emailConfirmation)
            false
        }
    }

    private fun getErrorStringResIdEmptyEmail(value: String): Int? =
        if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            R.string.invalid_email
        } else null

    private fun getErrorStringResIdEmailIsNotTheSame(
        email: String,
        emailConfirmation: String
    ): Int? =
        if (emailConfirmation != email) {
            R.string.confirm_email
        } else null

    private fun getErrorStringResIdInvalidName(
        name: String
    ): Int? =
        if (name.isEmpty()) {
            R.string.invalid_name
        } else null
}