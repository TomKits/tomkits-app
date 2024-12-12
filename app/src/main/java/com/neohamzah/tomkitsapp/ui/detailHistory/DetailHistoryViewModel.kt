package com.neohamzah.tomkitsapp.ui.detailHistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.neohamzah.tomkitsapp.data.pref.UserModel
import com.neohamzah.tomkitsapp.data.repository.Repository

class DetailHistoryViewModel (private val repository: Repository) : ViewModel() {
    fun getHistoryDetail(id:String, token: String)= repository.getDetailStory(id, token)

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}
