package com.dermaseer.dermaseer.data.network.responses

import com.google.gson.annotations.SerializedName

data class ProductResponse(

	@field:SerializedName("data")
	val data: List<DataProduct?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataProduct(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("shop_name")
	val shopName: String? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("product_rating")
	val productRating: Any? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
