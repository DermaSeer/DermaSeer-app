package com.dermaseer.dermaseer.data.remote.models


import com.google.gson.annotations.SerializedName

data class IngredientsRequest(
    @SerializedName("predict_id")
    val predictId: String?,
    @SerializedName("product_category")
    val productCategory: String?,
    @SerializedName("skin_type")
    val skinType: String?
)