package com.dermaseer.dermaseer.data.remote.models


import com.google.gson.annotations.SerializedName

data class HistoryResponse(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("acne_type")
        val acneType: String?,
        @SerializedName("createdAt")
        val createdAt: String?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("image_url")
        val imageUrl: String?,
        @SerializedName("result")
        val result: Result?
    ) {
        data class Result(
            @SerializedName("createdAt")
            val createdAt: String?,
            @SerializedName("id")
            val id: String?,
            @SerializedName("ingredient")
            val ingredient: List<String?>?,
            @SerializedName("msg_recommendation")
            val msgRecommendation: String?,
            @SerializedName("predict_id")
            val predictId: String?,
            @SerializedName("product_category")
            val productCategory: String?,
            @SerializedName("skin_type")
            val skinType: String?,
            @SerializedName("updatedAt")
            val updatedAt: String?
        )
    }
}