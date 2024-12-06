package com.dermaseer.dermaseer.data.remote.models


import com.google.gson.annotations.SerializedName

data class IngredientResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Boolean?
) {
    data class Data(
        @SerializedName("ingredient")
        val ingredient: List<String?>?,
        @SerializedName("message")
        val message: String?,
        @SerializedName("result_id")
        val resultId: String?
    )
}