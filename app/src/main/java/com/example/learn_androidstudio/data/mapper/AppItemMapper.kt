// app/src/main/java/com/example/learn_androidstudio/data/mapper/AppItemMapper.kt
package com.example.learn_androidstudio.data.mapper

import com.example.learn_androidstudio.data.dto.AppItemDto
import com.example.learn_androidstudio.domain.model.AppItem
import javax.inject.Inject

class AppItemMapper @Inject constructor() {

    fun mapDtoToDomain(dto: AppItemDto): AppItem {
        val rawIcon = dto.iconUrl.orEmpty()
        val fullIconUrl = if (rawIcon.startsWith("/")) {
            "http://185.103.109.134$rawIcon"
        } else {
            rawIcon
        }

        return AppItem(
            id = dto.id.orEmpty(),
            name = dto.name.orEmpty(),
            description = dto.description.orEmpty(),
            category = dto.category.orEmpty(),
            iconUrl = fullIconUrl,
            developer = dto.developer.takeUnless { it.isNullOrBlank() } ?: "VK Team",
            ageRating = dto.ageRating.takeUnless { it.isNullOrBlank() } ?: "12+",
            size = dto.size.takeUnless { it.isNullOrBlank() } ?: "95 МБ"
        )
    }

    fun mapDomainToDto(domain: AppItem): AppItemDto {
        return AppItemDto(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            category = domain.category,
            iconUrl = domain.iconUrl,
            developer = domain.developer,
            ageRating = domain.ageRating,
            size = domain.size
        )
    }
}