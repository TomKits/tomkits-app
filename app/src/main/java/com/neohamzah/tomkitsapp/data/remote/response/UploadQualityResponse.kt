package com.neohamzah.tomkitsapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class UploadQualityResponse (
    @field:SerializedName("confidence")
    val confidence: String? = null,
)