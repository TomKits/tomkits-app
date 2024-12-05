package com.neohamzah.tomkitsapp.ui.scanDisease

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.neohamzah.tomkitsapp.data.repository.Repository

class ScanDiseaseViewModel (
    private val repository: Repository,
) : ViewModel(){
    var currentImageUri: Uri? = null
}