package com.dermaseer.dermaseer.data.remote.models


import com.google.gson.annotations.SerializedName

data class PredictResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Data(
        @SerializedName("acne_type")
        val acneType: String?,
        @SerializedName("image_url")
        val imageUrl: String?,
        @SerializedName("predict_id")
        val predictId: String?
    )
}