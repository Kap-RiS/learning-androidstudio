package com.example.learn_androidstudio.data.mapper

import com.example.learn_androidstudio.data.dto.AppItemDto
import com.example.learn_androidstudio.domain.model.AppItem

// Функция расширения для конвертации одного объекта
fun AppItemDto.toDomain(): AppItem {
    return AppItem(
        id = id,
        name = title,
        description = subtitle,
        category = categoryName,
        iconRes = icon,
        developer = developer,
        ageRating = ageRating,
        size = size
    )
}

// Функция расширения для конвертации списка объектов
fun List<AppItemDto>.toDomain(): List<AppItem> {
    return map { it.toDomain() }
}