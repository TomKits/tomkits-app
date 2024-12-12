package com.neohamzah.tomkitsapp.ui.scanQuality

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.neohamzah.tomkitsapp.UserUploadRepository
import com.neohamzah.tomkitsapp.data.pref.UserModel
import com.neohamzah.tomkitsapp.data.remote.response.UploadQualityResponse
import com.neohamzah.tomkitsapp.data.repository.Repository
import com.neohamzah.tomkitsapp.utils.Result
import java.io.File

class ScanQualityViewModel(
    private val repository: Repository,
    private val userUploadRepository: UserUploadRepository
) : ViewModel() {
    var currentImageUri: Uri? = null

    // Initialize data as MutableLiveData
    private val _data = MutableLiveData<UploadQualityResponse>()
    val data: LiveData<UploadQualityResponse> get() = _data

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun imageUpload(file: File, token: String): LiveData<Result<UploadQualityResponse>> {
        return userUploadRepository.imageUploadQuality(file, token).apply {
            observeForever {
                if (it is Result.Success) {
                    _data.value = it.data
                }
            }
        }
    }
}
