package com.souzaemerson.marvelproject.data.repository.login

import com.souzaemerson.marvelproject.data.mockdatabase.MockDatabase
import com.souzaemerson.marvelproject.data.model.User

class LoginRepositoryMock(private val mock: MockDatabase): LoginRepository {
    override suspend fun userLogin(email: String, password: String): User =
        mock.mockLogin(email, password)
}