package com.example.learn_androidstudio.data.source

import com.example.learn_androidstudio.data.dto.AppItemDto

object MockAppDataSource {
    fun getAppDtos(): List<AppItemDto> = listOf(
        AppItemDto(
            id = "1",
            name = "VK",
            description = "Социальная сеть: общение, сообщества, музыка и видео.",
            category = "Соцсети",
            iconUrl = "",
            developer = "VK.com",
            ageRating = "12+",
            size = "145 МБ"
        ),
        AppItemDto(
            id = "2",
            name = "Яндекс Музыка",
            description = "Миллионы треков, персональные рекомендации и подкасты.",
            category = "Музыка",
            iconUrl = "",
            developer = "Yandex LLC",
            ageRating = "12+",
            size = "62 МБ"
        )
    )
}