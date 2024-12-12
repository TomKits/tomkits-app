package com.neohamzah.tomkitsapp.data.remote

import com.neohamzah.tomkitsapp.data.remote.response.DetailHistoryResponse
import com.neohamzah.tomkitsapp.data.remote.response.HistoryResponse
import com.neohamzah.tomkitsapp.data.remote.response.LoginResponse
import com.neohamzah.tomkitsapp.data.remote.response.RegisterRequest
import com.neohamzah.tomkitsapp.data.remote.response.RegisterResponse
import com.neohamzah.tomkitsapp.data.remote.response.UploadDiseaseResponse
import com.neohamzah.tomkitsapp.data.remote.response.UploadQualityResponse
import com.neohamzah.tomkitsapp.data.remote.response.UserResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path


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

    @GET("auth/whoami")
    suspend fun getUserInfo(
        @Header("Authorization") token: String,
    ): UserResponse

    @Multipart
    @POST("predict/disease")
    suspend fun uploadDisease(
        @Part file: MultipartBody.Part,
        @Header("Authorization") token: String
    ): UploadDiseaseResponse

    @Multipart
    @POST("predict/tomato")
    suspend fun uploadQuality(
        @Part file: MultipartBody.Part,
        @Header("Authorization") token: String
    ): UploadQualityResponse

    @GET("predict/history")
    suspend fun getAllHistory(
        @Header("Authorization") token: String,
    ): HistoryResponse

    @GET("predict/history/{prediction_id}")
    suspend fun getDetailHistory(
        @Header("Authorization") token: String,
        @Path("prediction_id") predictionId: String
    ): DetailHistoryResponse
}








