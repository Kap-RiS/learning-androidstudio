package com.example.learn_androidstudio.data.dto

import androidx.annotation.DrawableRes

data class AppItemDto(
    val id: Int,
    val title: String,
    val subtitle: String,
    val categoryName: String,
    @DrawableRes val icon: Int,
    val developer: String, // Новый параметр: разработчик
    val ageRating: String, // Новый параметр: возрастное ограничение (напр. "12+")
    val size: String       // Новый параметр: размер (напр. "145 МБ")
)