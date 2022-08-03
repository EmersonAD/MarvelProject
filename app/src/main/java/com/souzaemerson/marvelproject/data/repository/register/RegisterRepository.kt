package com.souzaemerson.marvelproject.data.repository.register

import com.souzaemerson.marvelproject.data.model.User

interface RegisterRepository {
    suspend fun registerNewUser(user: User)
}