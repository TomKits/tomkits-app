package com.neohamzah.tomkitsapp.data.repository

import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.neohamzah.tomkitsapp.data.model.RegisterRequest
import com.neohamzah.tomkitsapp.data.model.RegisterResponse
import com.neohamzah.tomkitsapp.data.model.UserModel
import com.neohamzah.tomkitsapp.data.remote.ApiService
import com.neohamzah.tomkitsapp.preference.UserPreference
import com.neohamzah.tomkitsapp.utils.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class Repository private constructor(
    private val userPreference: UserPreference,
    private var apiService: ApiService
) {
    suspend fun register(username: String, email: String, password: String): Result<RegisterResponse> {
        val request = RegisterRequest(username, email, password)
        return try {
            val response = apiService.register(request)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(response.errorBody()?.string() ?: "Unknown error")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Network error")
        }
    }
    fun login(email: String, password: String) = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.login(mapOf("email" to email, "password" to password))
            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    val accessToken = loginResponse.token?.access ?: ""
                    val refreshToken = loginResponse.token?.refresh ?: ""

                    val userModel = UserModel(
                        email = email,
                        token = accessToken,
                        isLogin = true
                    )
                    saveSession(userModel)
                    emit(Result.Success(loginResponse))
                } ?: run {
                    emit(Result.Error("Empty response body"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                emit(Result.Error(errorBody ?: "Unknown error"))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            emit(Result.Error("HTTP Error: ${errorBody ?: "Unknown error"}"))
        } catch (e: Exception) {
            emit(Result.Error("Exception: ${e.message ?: "Unknown error"}"))
        }
    }
    fun refreshToken(refreshToken: String) = liveData {
        try {
            val response = apiService.refreshToken(refreshToken)
            val newAccessToken = response.token?.access ?: ""
            emit(Result.Success(newAccessToken))
        } catch (e: HttpException) {
            emit(Result.Error("Gagal memperbarui token"))
        }
    }
    private fun parseError(e: HttpException): String {
        val errorBody = e.response()?.errorBody()?.string()
        return if (!errorBody.isNullOrEmpty()) {
            try {
                val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                errorResponse.message ?: "Something went wrong"
            } catch (ex: Exception) {
                "Error parsing response"
            }
        } else {
            "Server Error: ${e.message()}"
        }
    }

    private suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: Repository? = null

        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(userPreference, apiService)
            }.also { instance = it }
    }
}
