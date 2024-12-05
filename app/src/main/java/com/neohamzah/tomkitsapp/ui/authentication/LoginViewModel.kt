package com.neohamzah.tomkitsapp.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.neohamzah.tomkitsapp.data.remote.response.LoginResponse
import com.neohamzah.tomkitsapp.data.repository.Repository
import com.neohamzah.tomkitsapp.utils.Result

class LoginViewModel(private val repository: Repository) : ViewModel() {
    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> get() = _loginResult

    fun login(email: String, password: String) {
        repository.login(email, password).observeForever {
            _loginResult.postValue(it)
        }
    }
}
