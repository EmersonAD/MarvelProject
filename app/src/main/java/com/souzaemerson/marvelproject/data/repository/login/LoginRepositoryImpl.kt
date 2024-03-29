package com.souzaemerson.marvelproject.data.repository.login

import com.souzaemerson.marvelproject.data.db.dao.UserDAO
import com.souzaemerson.marvelproject.data.model.User
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val dao: UserDAO): LoginRepository {
    override suspend fun userLogin(email: String, password: String): User? =
         dao.getValidUser(email, password)
}