package com.souzaemerson.marvelproject.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    val email: String,
    val name: String,
    val password: String,
    val photo: Int?,
    val favorites: List<Results>
)
