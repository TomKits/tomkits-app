package com.neohamzah.tomkitsapp.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neohamzah.tomkitsapp.data.model.RegisterResponse
import com.neohamzah.tomkitsapp.data.repository.Repository
import com.neohamzah.tomkitsapp.utils.Result
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    private val _registerResult = MutableLiveData<Result<RegisterResponse>>()
    val registerResult: LiveData<Result<RegisterResponse>> = _registerResult

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _registerResult.value = Result.Loading
            val result = repository.register(name, email, password)
            _registerResult.value = result
        }
    }
}
