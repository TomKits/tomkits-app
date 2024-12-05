package com.neohamzah.tomkitsapp.ui.scanQuality

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.neohamzah.tomkitsapp.data.repository.Repository

class ScanQualityViewModel (
    private val repository: Repository,
) : ViewModel(){
    var currentImageUri: Uri? = null
}