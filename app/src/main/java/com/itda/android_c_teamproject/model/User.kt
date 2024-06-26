package com.itda.android_c_teamproject.model

data class User(
    val username: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val dateOfBirth: String,
    val userGender: String,
    val userWeight: Int,
    val userHeight: Double,
)