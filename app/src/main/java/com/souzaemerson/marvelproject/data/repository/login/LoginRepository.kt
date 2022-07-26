package com.souzaemerson.marvelproject.data.repository.login

import com.souzaemerson.marvelproject.data.model.User

interface LoginRepository {
    suspend fun login(email: String, password: String): User?
}