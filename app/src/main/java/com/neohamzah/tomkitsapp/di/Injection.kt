package com.neohamzah.tomkitsapp.di

import android.content.Context
import com.neohamzah.tomkitsapp.UserUploadRepository
import com.neohamzah.tomkitsapp.data.pref.UserPreference
import com.neohamzah.tomkitsapp.data.pref.dataStore
import com.neohamzah.tomkitsapp.data.remote.ApiConfig
import com.neohamzah.tomkitsapp.data.repository.Repository

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiServices()
        return Repository.getInstance(pref, apiService)
    }

    fun provideUploadRepository(): UserUploadRepository {
        val apiService = ApiConfig.getApiServices()
        return UserUploadRepository.getInstance(apiService)
    }
}