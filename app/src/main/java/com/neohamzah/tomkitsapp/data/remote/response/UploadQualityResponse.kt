package com.neohamzah.tomkitsapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class UploadQualityResponse (
    @field:SerializedName("Quality")
    val quality: Quality? = null,

    @field:SerializedName("Type")
    val type: Type? = null,

    @field:SerializedName("Response Text")
    val message: String? = null
)

data class Quality (
    @field:SerializedName("Class")
    val classQuality: String? = null,

    @field:SerializedName("Confidence")
    val confidenceQuality: String? = null
)

data class Type (
    @field:SerializedName("Class")
    val classType: String? = null,

    @field:SerializedName("Confidence")
    val confidenceType: String? = null
)