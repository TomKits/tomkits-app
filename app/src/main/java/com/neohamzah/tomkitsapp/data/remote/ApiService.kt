package com.neohamzah.tomkitsapp.data.remote

import com.neohamzah.tomkitsapp.data.remote.response.LoginResponse
import com.neohamzah.tomkitsapp.data.remote.response.RegisterRequest
import com.neohamzah.tomkitsapp.data.remote.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST


interface ApiService {
    @POST("auth/register")
    @Headers("Content-Type: application/json")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @Headers("Content-Type: application/json")
    @POST("auth/login")
    suspend fun login(
        @Body requestBody: Map<String, String>
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("refresh")
    suspend fun refreshToken(
        @Field("refresh") refreshToken: String
    ): LoginResponse
}








