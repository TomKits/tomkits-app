package com.neohamzah.tomkitsapp.ui.scanDisease

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.neohamzah.tomkitsapp.data.pref.UserModel
import com.neohamzah.tomkitsapp.data.repository.Repository
import java.io.File

class ScanDiseaseViewModel (
    private val repository: Repository,
) : ViewModel(){
    var currentImageUri: Uri? = null
}