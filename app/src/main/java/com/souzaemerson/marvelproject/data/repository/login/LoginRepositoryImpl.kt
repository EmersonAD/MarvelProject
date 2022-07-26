package com.souzaemerson.marvelproject.data.repository.login

import com.souzaemerson.marvelproject.data.db.CharacterDAO
import com.souzaemerson.marvelproject.data.model.User

class LoginRepositoryImpl(private val dao: CharacterDAO): LoginRepository {
    override suspend fun login(email: String, password: String): User? =
         dao.getValidUser(email, password)

}