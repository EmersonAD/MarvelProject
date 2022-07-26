package com.souzaemerson.marvelproject.data.repository.login

import com.souzaemerson.marvelproject.data.mockdatabase.MockDatabase
import com.souzaemerson.marvelproject.data.model.User

class LoginRepositoryMock: LoginRepository {
    override suspend fun login(email: String, password: String): User =
        MockDatabase.mockLogin(email, password)
}