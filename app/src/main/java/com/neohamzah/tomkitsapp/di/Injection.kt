package com.neohamzah.tomkitsapp.di

import android.content.Context
import com.neohamzah.tomkitsapp.data.remote.ApiConfig
import com.neohamzah.tomkitsapp.data.repository.Repository
import com.neohamzah.tomkitsapp.preference.UserPreference
import com.neohamzah.tomkitsapp.preference.dataStore

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiServices()
        return Repository.getInstance(pref, apiService)
    }
}