package com.example.learn_androidstudio.data.source

import com.example.learn_androidstudio.R
import com.example.learn_androidstudio.data.dto.AppItemDto

object MockAppDataSource {
    fun getAppDtos(): List<AppItemDto> = listOf(
        AppItemDto(
            id = 1,
            title = "VK",
            subtitle = "Социальная сеть: общение, сообщества, музыка и видео.",
            categoryName = "Соцсети",
            icon = R.drawable.ic_vk,
            developer = "VK.com",
            ageRating = "12+",
            size = "145 МБ"
        ),
        AppItemDto(
            id = 2,
            title = "Яндекс Музыка",
            subtitle = "Миллионы треков, персональные рекомендации и подкасты.",
            categoryName = "Музыка",
            icon = R.drawable.ic_yandexmusic,
            developer = "Yandex LLC",
            ageRating = "12+",
            size = "62 МБ"
        ),
        AppItemDto(
            id = 3,
            title = "СберБанк Онлайн",
            subtitle = "Платежи, переводы, вклады и финансовые сервисы.",
            categoryName = "Финансы",
            icon = R.drawable.ic_sber,
            developer = "ПАО Сбербанк",
            ageRating = "6+",
            size = "210 МБ"
        ),
        AppItemDto(
            id = 4,
            title = "Wildberries",
            subtitle = "Маркетплейс с миллионами товаров и быстрой доставкой.",
            categoryName = "Покупки",
            icon = R.drawable.ic_wb,
            developer = "WB LLC",
            ageRating = "12+",
            size = "110 МБ"
        ),
        AppItemDto(
            id = 5,
            title = "Кинопоиск",
            subtitle = "Фильмы и сериалы в HD качестве с удобным поиском.",
            categoryName = "Развлечения",
            icon = R.drawable.ic_kinopoisk,
            developer = "Yandex LLC",
            ageRating = "18+",
            size = "88 МБ"
        ),
        AppItemDto(
            id = 6,
            title = "Яндекс Карты",
            subtitle = "Карты, маршруты, пробки и поиск организаций.",
            categoryName = "Навигация",
            icon = R.drawable.ic_yandexmaps,
            developer = "Yandex LLC",
            ageRating = "0+",
            size = "130 МБ"
        ),
        AppItemDto(
            id = 7,
            title = "VK Видео",
            subtitle = "Смотри фильмы и шоу.",
            categoryName = "Видео",
            icon = R.drawable.ic_vkvideo,
            developer = "VK.com",
            ageRating = "12+",
            size = "95 МБ"
        ),
        AppItemDto(
            id = 8,
            title = "Почта Mail.ru",
            subtitle = "Удобная электронная почта.",
            categoryName = "Утилиты",
            icon = R.drawable.ic_mail,
            developer = "VK.com",
            ageRating = "3+",
            size = "54 МБ"
        ),
        AppItemDto(
            id = 9,
            title = "Дзен",
            subtitle = "Читай интересные статьи.",
            categoryName = "Новости",
            icon = R.drawable.ic_dzen,
            developer = "VK.com",
            ageRating = "12+",
            size = "70 МБ"
        ),
        AppItemDto(
            id = 10,
            title = "Telegram",
            subtitle = "Чаты, общение, каналы",
            categoryName = "Мессенджер",
            icon = R.drawable.ic_tg,
            developer = "Telegram FZ-LLC",
            ageRating = "16+",
            size = "95 МБ"
        )
    )
}