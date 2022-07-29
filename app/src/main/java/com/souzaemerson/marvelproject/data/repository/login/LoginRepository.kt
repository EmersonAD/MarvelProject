package com.souzaemerson.marvelproject.data.repository.login

import com.souzaemerson.marvelproject.data.model.User

interface LoginRepository {
    suspend fun userLogin(email: String, password: String): User?
}