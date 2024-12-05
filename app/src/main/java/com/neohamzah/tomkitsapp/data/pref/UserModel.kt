package com.neohamzah.tomkitsapp.data.pref

data class UserModel (
    val email: String,
    val token: String,
    val refreshToken: String,
    val isLogin: Boolean = false
)


