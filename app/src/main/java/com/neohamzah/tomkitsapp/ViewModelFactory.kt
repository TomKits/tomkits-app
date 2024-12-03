package com.neohamzah.tomkitsapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.neohamzah.tomkitsapp.data.repository.Repository
import com.neohamzah.tomkitsapp.di.Injection
import com.neohamzah.tomkitsapp.ui.authentication.LoginViewModel
import com.neohamzah.tomkitsapp.ui.authentication.RegisterViewModel
import com.neohamzah.tomkitsapp.ui.main.MainViewModel

class ViewModelFactory(
        private val repository: Repository,
    ) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                    MainViewModel(repository) as T
                }
                modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                    RegisterViewModel(repository) as T
                }
                modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                    LoginViewModel(repository) as T
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }

        companion object {
            @Volatile
            private var INSTANCE: ViewModelFactory? = null
            @JvmStatic
            fun getInstance(context: Context): ViewModelFactory {
                return INSTANCE ?: synchronized(this) {
                    val repository = Injection.provideRepository(context)
                    ViewModelFactory(repository).also {
                        INSTANCE = it
                    }
                }
            }
        }
    }
