package com.neohamzah.tomkitsapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.neohamzah.tomkitsapp.data.pref.UserModel
import com.neohamzah.tomkitsapp.data.repository.Repository
import kotlinx.coroutines.launch

class ProfileViewModel (
    private val repository: Repository,
) : ViewModel(){

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getUserInfo(token: String) = repository.getUserInfo(token)

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}