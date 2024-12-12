package com.neohamzah.tomkitsapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(

	@field:SerializedName("histories")
	val histories: List<HistoriesItem?>? = null
)

data class HistoriesItem(

	@field:SerializedName("image_link")
	val imageLink: String? = null,

	@field:SerializedName("disease_name")
	val diseaseName: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null
)
