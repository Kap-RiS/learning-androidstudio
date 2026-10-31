package com.example.learn_androidstudio.domain.model

import androidx.annotation.DrawableRes

data class AppItem(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    @DrawableRes val iconRes: Int,
    val developer: String,
    val ageRating: String,
    val size: String
)