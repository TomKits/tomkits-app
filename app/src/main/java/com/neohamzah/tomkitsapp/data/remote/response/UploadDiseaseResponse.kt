package com.neohamzah.tomkitsapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class UploadDiseaseResponse (
    @field:SerializedName("confidence")
    val confidence: String? = null,

    @field:SerializedName("deskripsi")
    val description: String? = null,

    @field:SerializedName("nama_penyakit")
    val diseaseName: String? = null,

    @field:SerializedName("rekomendasi_product")
    val productList: List<ProductList?>,

    @field:SerializedName("solusi")
    val solution: String? = null,

    @field:SerializedName("Response Text")
    val message: String? = null
)

data class ProductList (
    @field:SerializedName("active_ingredient")
    val activeIngredient: String? = null,

    @field:SerializedName("product_image")
    val productImage: String? = null,

    @field:SerializedName("product_link")
    val productLink: String? = null,

    @field:SerializedName("product_name")
    val productName: String? = null,
)