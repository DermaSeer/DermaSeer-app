package com.dermaseer.dermaseer.data.remote.models


import com.google.gson.annotations.SerializedName

data class ArticleResponse(
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
        @SerializedName("content")
        val content: String?,
        @SerializedName("createdAt")
        val createdAt: String?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("image_url")
        val imageUrl: String?,
        @SerializedName("publish_date")
        val publishDate: Any?,
        @SerializedName("title")
        val title: String?,
        @SerializedName("updatedAt")
        val updatedAt: String?,
        @SerializedName("url")
        val url: String?
    )

    data class Meta(
        @SerializedName("page")
        val page: Int?,
        @SerializedName("size")
        val size: Int?,
        @SerializedName("totalItems")
        val totalItems: Int?
    )
}