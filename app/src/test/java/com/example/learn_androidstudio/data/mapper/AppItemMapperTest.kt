package com.example.learn_androidstudio.data.mapper

import com.example.learn_androidstudio.data.dto.AppItemDto
import com.example.learn_androidstudio.domain.model.AppItem
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AppItemMapperTest {

    private lateinit var mapper: AppItemMapper

    @Before
    fun setUp() {
        mapper = AppItemMapper()
    }

    @Test
    fun `mapDtoToDomain should correctly map full DTO to Domain model`() {
        // Given
        val dto = AppItemDto(
            id = "app_1",
            name = "VK Messenger",
            description = "Быстрые сообщения",
            category = "Общение",
            iconUrl = "https://example.com/icon.png",
            developer = "VK LLC",
            ageRating = "16+",
            size = "85 МБ"
        )

        // When
        val domain = mapper.mapDtoToDomain(dto)

        // Then
        assertEquals("app_1", domain.id)
        assertEquals("VK Messenger", domain.name)
        assertEquals("Быстрые сообщения", domain.description)
        assertEquals("Общение", domain.category)
        assertEquals("https://example.com/icon.png", domain.iconUrl)
        assertEquals("VK LLC", domain.developer)
        assertEquals("16+", domain.ageRating)
        assertEquals("85 МБ", domain.size)
    }

    @Test
    fun `mapDtoToDomain should substitute default values when optional fields are null`() {
        // Given
        val dto = AppItemDto(
            id = "app_2",
            name = "Test App",
            description = "Description",
            category = "Tools",
            iconUrl = null,
            developer = null,
            ageRating = null,
            size = null
        )

        // When
        val domain = mapper.mapDtoToDomain(dto)

        // Then
        assertEquals("app_2", domain.id)
        assertEquals("", domain.iconUrl)
        assertEquals("VK Team", domain.developer)
        assertEquals("12+", domain.ageRating)
        assertEquals("95 МБ", domain.size)
    }

    @Test
    fun `mapDtoToDomain should prepend base url when iconUrl is relative path`() {
        // Given
        val dto = AppItemDto(
            id = "app_3",
            name = "App With Relative Icon",
            description = "Description",
            category = "Games",
            iconUrl = "/static/images/logo.png"
        )

        // When
        val domain = mapper.mapDtoToDomain(dto)

        // Then
        assertEquals("http://185.103.109.134/static/images/logo.png", domain.iconUrl)
    }

    @Test
    fun `mapDomainToDto should correctly map Domain model to DTO`() {
        // Given
        val domain = AppItem(
            id = "app_4",
            name = "Music Player",
            description = "Слушай любимую музыку",
            category = "Музыка",
            iconUrl = "https://example.com/music.png",
            developer = "Yandex LLC",
            ageRating = "6+",
            size = "50 МБ"
        )

        // When
        val dto = mapper.mapDomainToDto(domain)

        // Then
        assertEquals("app_4", dto.id)
        assertEquals("Music Player", dto.name)
        assertEquals("Слушай любимую музыку", dto.description)
        assertEquals("Музыка", dto.category)
        assertEquals("https://example.com/music.png", dto.iconUrl)
        assertEquals("Yandex LLC", dto.developer)
        assertEquals("6+", dto.ageRating)
        assertEquals("50 МБ", dto.size)
    }

    @Test
    fun `mapDtoToDomain should substitute defaults when developer age and size are blank`() {
        // Given
        val dto = AppItemDto(
            id = "app_5",
            name = "Blank Fields App",
            description = "Desc",
            category = "Utility",
            developer = "   ",
            ageRating = "",
            size = " "
        )

        // When
        val domain = mapper.mapDtoToDomain(dto)

        // Then
        assertEquals("VK Team", domain.developer)
        assertEquals("12+", domain.ageRating)
        assertEquals("95 МБ", domain.size)
    }
}