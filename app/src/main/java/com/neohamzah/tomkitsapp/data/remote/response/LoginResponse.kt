package com.neohamzah.tomkitsapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("token")
	val token: Token? = null
)

data class Token(

	@field:SerializedName("access")
	val access: String? = null,

	@field:SerializedName("refresh")
	val refresh: String? = null
)
