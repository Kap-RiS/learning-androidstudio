package com.example.learn_androidstudio.data.repository

import com.example.learn_androidstudio.data.api.AppApiService
import com.example.learn_androidstudio.data.dto.AppItemDto
import com.example.learn_androidstudio.data.mapper.AppItemMapper
import com.example.learn_androidstudio.domain.model.AppItem
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AppRepositoryImplTest {

    private val apiService: AppApiService = mockk()
    private val mapper: AppItemMapper = mockk()
    private lateinit var repository: AppRepositoryImpl

    private val sampleDto = AppItemDto(id = "1", name = "VK", description = "Social", category = "Social")
    private val sampleDomain = AppItem(
        id = "1",
        name = "VK",
        description = "Social",
        category = "Social",
        developer = "VK.com",
        ageRating = "12+",
        size = "145 МБ"
    )

    @Before
    fun setUp() {
        repository = AppRepositoryImpl(apiService, mapper)
    }

    @Test
    fun `getCatalog should return mapped apps when apiService succeeds`() = runTest {
        // Given
        coEvery { apiService.getCatalog() } returns listOf(sampleDto)
        every { mapper.mapDtoToDomain(sampleDto) } returns sampleDomain

        // When
        val result = repository.getCatalog()

        // Then
        assertEquals(1, result.size)
        assertEquals(sampleDomain.id, result.first().id)
        assertEquals(sampleDomain.name, result.first().name)
    }

    @Test
    fun `getCatalog should fallback to mock data when apiService throws exception`() = runTest {
        // Given
        coEvery { apiService.getCatalog() } throws RuntimeException("Network error")
        every { mapper.mapDtoToDomain(any()) } answers {
            val dto = firstArg<AppItemDto>()
            AppItem(
                id = dto.id.orEmpty(),
                name = dto.name.orEmpty(),
                description = dto.description.orEmpty(),
                category = dto.category.orEmpty(),
                developer = "Mock Dev",
                ageRating = "12+",
                size = "50 МБ"
            )
        }

        // When
        val result = repository.getCatalog()

        // Then
        assertTrue(result.isNotEmpty())
        assertEquals("1", result.first().id)
    }

    @Test
    fun `getAppDetail should return domain app when apiService succeeds`() = runTest {
        // Given
        coEvery { apiService.getAppDetail("1") } returns sampleDto
        every { mapper.mapDtoToDomain(sampleDto) } returns sampleDomain

        // When
        val result = repository.getAppDetail("1")

        // Then
        assertEquals("1", result.id)
        assertEquals("VK", result.name)
    }

    @Test
    fun `getAppDetail should fallback to mock when api fails and app exists in mock`() = runTest {
        // Given
        coEvery { apiService.getAppDetail("1") } throws RuntimeException("404 Not Found")
        coEvery { apiService.getCatalog() } throws RuntimeException("Network error")
        every { mapper.mapDtoToDomain(any()) } answers {
            val dto = firstArg<AppItemDto>()
            AppItem(
                id = dto.id.orEmpty(),
                name = dto.name.orEmpty(),
                description = dto.description.orEmpty(),
                category = dto.category.orEmpty(),
                developer = "Mock Dev",
                ageRating = "12+",
                size = "50 МБ"
            )
        }

        // When
        val result = repository.getAppDetail("1")

        // Then
        assertEquals("1", result.id)
        assertNotNull(result)
    }

    @Test
    fun `loadRatingsInParallel should return rating for all passed ids`() = runTest {
        // Given
        val ids = listOf("app_1", "app_2", "app_3")

        // When
        val ratings = repository.loadRatingsInParallel(ids)

        // Then
        assertEquals(3, ratings.size)
        ids.forEach { id ->
            assertTrue(ratings.containsKey(id))
            val rating = ratings[id]!!
            assertTrue(rating in 3.0f..5.0f)
        }
    }

    @Test
    fun `isAppInstalled and setAppInstalled should properly update install state`() {
        // Given
        val testAppId = "app_test_100"
        assertFalse(repository.isAppInstalled(testAppId))

        // When
        repository.setAppInstalled(testAppId, true)

        // Then
        assertTrue(repository.isAppInstalled(testAppId))

        // When
        repository.setAppInstalled(testAppId, false)

        // Then
        assertFalse(repository.isAppInstalled(testAppId))
    }
}