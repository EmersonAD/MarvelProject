package com.souzaemerson.marvelproject.data.repository.register

import com.souzaemerson.marvelproject.data.db.dao.UserDAO
import com.souzaemerson.marvelproject.data.model.User
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(private val dao: UserDAO): RegisterRepository {
    override suspend fun registerNewUser(user: User) {
        dao.createNewUser(user)
    }
}