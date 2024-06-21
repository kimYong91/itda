package com.itda.android_c_teamproject.model.dto

data class UserDTO(
    val username: String,
    val email: String,
    val phoneNumber: String,
    val dateOfBirth: String,
    val userAge: Int,
    val userGender: String,
    val userWeight: Int,
    val userHeight: Double,
    val basalMetabolism: Double

)
