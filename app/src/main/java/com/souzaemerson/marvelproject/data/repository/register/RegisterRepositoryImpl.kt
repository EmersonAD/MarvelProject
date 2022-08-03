package com.souzaemerson.marvelproject.data.repository.register

import com.souzaemerson.marvelproject.data.db.daos.UserDAO
import com.souzaemerson.marvelproject.data.model.User

class RegisterRepositoryImpl(private val dao: UserDAO): RegisterRepository {
    override suspend fun registerNewUser(user: User) {
        dao.createNewUser(user)
    }
}