package com.souzaemerson.marvelproject.data.repository.login

import com.souzaemerson.marvelproject.data.db.daos.UserDAO
import com.souzaemerson.marvelproject.data.model.User

class LoginRepositoryImpl(private val dao: UserDAO): LoginRepository {
    override suspend fun userLogin(email: String, password: String): User? =
         dao.getValidUser(email, password)
}