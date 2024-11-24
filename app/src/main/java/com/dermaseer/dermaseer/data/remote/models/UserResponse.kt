package com.dermaseer.dermaseer.data.remote.models


import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("user")
        val user: User?
    ) {
        data class User(
            @SerializedName("birthday")
            val birthday: String?,
            @SerializedName("email")
            val email: String?,
            @SerializedName("gender")
            val gender: String?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("profile_picture")
            val profilePicture: String?
        )
    }
}