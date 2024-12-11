package com.neohamzah.tomkitsapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("user_details")
    val userDetail: UserDetails? = null,
)

data class UserDetails(
    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("username")
    val username: String? = null,
)
