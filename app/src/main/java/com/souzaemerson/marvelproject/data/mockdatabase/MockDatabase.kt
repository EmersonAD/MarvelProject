package com.souzaemerson.marvelproject.data.mockdatabase

import com.souzaemerson.marvelproject.data.model.User
import java.lang.IllegalArgumentException
import kotlin.jvm.Throws

object MockDatabase {
    private var user = hashSetOf<User>()

    init {
        mockUser()
    }

    private fun mockUser() = user.run {
        add(User("hectorfortuna@gmail.com", "Hector", "12345678", null))
        add(User("emersonsoares@gmail.com", "Emerson", "12345678", null))
        add(User("amanddaluz@gmail.com", "Amanda", "12345678", null))
        add(User("fernanda@gmail.com", "Fernanda", "12345678", null))
        add(User("lieberson@gmail.com", "Lieberson", "12345678", null))
    }

    @Throws(Throwable::class)
    fun mockLogin(email: String, password: String): User {
        return this.user.firstOrNull { email == it.email && password == it.password }
            ?: throw IllegalArgumentException("Email e/ou senha inválido")
    }
}