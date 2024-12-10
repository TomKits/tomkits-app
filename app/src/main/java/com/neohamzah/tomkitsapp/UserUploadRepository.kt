package com.neohamzah.tomkitsapp

import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.neohamzah.tomkitsapp.data.remote.ApiService
import com.neohamzah.tomkitsapp.data.remote.response.UploadDiseaseResponse
import com.neohamzah.tomkitsapp.utils.Result
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class UserUploadRepository private constructor(
    private val apiService: ApiService,
){
    fun imageUploadDisease(imageFile: File, token:String) = liveData {
        emit(Result.Loading)
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "file",
            imageFile.name,
            requestImageFile
        )
    try {
        val successResponse = apiService.uploadDisease(multipartBody, "Bearer $token")
        emit(Result.Success(successResponse))
    } catch (e:HttpException){
        val errorBody = e.response()?.errorBody()?.string()
        val errorResponse = Gson().fromJson(errorBody, UploadDiseaseResponse::class.java)
        emit(Result.Error(errorResponse.message.toString()))

    }
}

    companion object {
        @Volatile
        private var instance: UserUploadRepository? = null
        fun getInstance(apiService: ApiService) =
            instance ?: synchronized(this) {
                instance ?: UserUploadRepository(apiService)
            }.also { instance = it }
    }
}