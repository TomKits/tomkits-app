package com.neohamzah.tomkitsapp.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@SerializedName("message")
	val message: String? = null,

	@SerializedName("token")
	val token: Token? = null
)

data class Token(
	@SerializedName("access")
	val access: String? = null,

	@SerializedName("refresh")
	val refresh: String? = null
)
