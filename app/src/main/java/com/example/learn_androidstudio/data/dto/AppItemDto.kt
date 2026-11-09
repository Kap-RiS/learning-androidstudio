package com.example.learn_androidstudio.data.dto

import com.google.gson.annotations.SerializedName

data class AppItemDto(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("name", alternate = ["title"])
    val name: String? = null,

    @SerializedName("description", alternate = ["subtitle"])
    val description: String? = null,

    @SerializedName("category", alternate = ["categoryName"])
    val category: String? = null,

    @SerializedName("icon", alternate = ["iconUrl"])
    val iconUrl: String? = null,

    @SerializedName("developer")
    val developer: String? = null,

    @SerializedName("ageRating", alternate = ["age_rating"])
    val ageRating: String? = null,

    @SerializedName("size")
    val size: String? = null
)