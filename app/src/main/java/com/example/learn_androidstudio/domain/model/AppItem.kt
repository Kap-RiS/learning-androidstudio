package com.example.learn_androidstudio.domain.model

import androidx.annotation.DrawableRes

data class AppItem(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val iconUrl: String = "",
    @DrawableRes val iconRes: Int = 0,
    val developer: String,
    val ageRating: String,
    val size: String
)