package com.dermaseer.dermaseer.data.remote.models


import com.google.gson.annotations.SerializedName

data class ProductRecommendationResponse(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("meta")
    val meta: Meta?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("category")
        val category: String?,
        @SerializedName("createdAt")
        val createdAt: String?,
        @SerializedName("description")
        val description: String?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("image_url")
        val imageUrl: String?,
        @SerializedName("matchCount")
        val matchCount: Int?,
        @SerializedName("matchedIngredients")
        val matchedIngredients: List<String?>?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("price")
        val price: Int?,
        @SerializedName("product_rating")
        val productRating: Double?,
        @SerializedName("shop_name")
        val shopName: String?,
        @SerializedName("updatedAt")
        val updatedAt: String?,
        @SerializedName("url")
        val url: String?
    )

    data class Meta(
        @SerializedName("page")
        val page: Int?,
        @SerializedName("total_items")
        val totalItems: Int?,
        @SerializedName("total_page")
        val totalPage: Int?
    )
}