package com.neohamzah.tomkitsapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailHistoryResponse(

	@field:SerializedName("image_link")
	val imageLink: String? = null,

	@field:SerializedName("solution")
	val solution: String? = null,

	@field:SerializedName("confidence")
	val confidence: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("disease_name")
	val diseaseName: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("product_list")
	val productList: List<ProductListItem?>? = null
)

data class ProductListItem(

	@field:SerializedName("product_image")
	val productImage: String? = null,

	@field:SerializedName("product_name")
	val productName: String? = null,

	@field:SerializedName("active_ingredient")
	val activeIngredient: String? = null,

	@field:SerializedName("product_link")
	val productLink: String? = null
)
