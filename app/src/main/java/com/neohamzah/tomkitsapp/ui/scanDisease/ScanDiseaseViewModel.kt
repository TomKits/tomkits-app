package com.neohamzah.tomkitsapp.ui.scanDisease

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.neohamzah.tomkitsapp.UserUploadRepository
import com.neohamzah.tomkitsapp.data.pref.UserModel
import com.neohamzah.tomkitsapp.data.repository.Repository
import java.io.File

class ScanDiseaseViewModel (
    private val repository: Repository,
    private val userUploadRepository : UserUploadRepository

) : ViewModel(){
    var currentImageUri: Uri? = null

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
    fun imageUpload(file: File, token: String) = userUploadRepository.imageUploadDisease(file, token)
}
