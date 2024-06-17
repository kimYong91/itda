package com.itda.android_c_teamproject.model

import java.util.Date

data class User (
    val username: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val userAge: Int,
    val userGender: Char,
    val userWeight: Int,
    val userHeight: Double,
    val basalMetabolism: Int,
    val joinDate: Date
)