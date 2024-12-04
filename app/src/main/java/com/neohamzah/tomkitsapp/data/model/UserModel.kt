package com.neohamzah.tomkitsapp.data.model

class UserModel (
    val email: String,
    val token: String,
    val refreshToken: String,
    val isLogin: Boolean = false
)


