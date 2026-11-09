package com.example.learn_androidstudio.data.mapper

import com.example.learn_androidstudio.R
import com.example.learn_androidstudio.data.dto.AppItemDto
import com.example.learn_androidstudio.domain.model.AppItem

fun AppItemDto.toDomain(): AppItem {
    return AppItem(
        id = id.orEmpty(), // Превращает String? в String (если null, будет пустая строка)
        name = name.orEmpty(),
        description = description.orEmpty(),
        category = category ?: "Разное",
        iconUrl = iconUrl.orEmpty(),
        iconRes = R.drawable.ic_launcher_foreground,
        developer = developer ?: "Неизвестный разработчик",
        ageRating = ageRating ?: "0+",
        size = size ?: "0 МБ"
    )
}

fun List<AppItemDto>.toDomain(): List<AppItem> {
    return map { it.toDomain() }
}