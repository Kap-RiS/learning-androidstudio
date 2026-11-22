package com.example.learn_androidstudio.domain.usecase

import com.example.learn_androidstudio.domain.model.AppItem
import com.example.learn_androidstudio.domain.repository.AppRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UseCasesTest {

    private val repository: AppRepository = mockk()
    private lateinit var getAppsUseCase: GetAppsUseCase
    private lateinit var getAppDetailUseCase: GetAppDetailUseCase

    private val sampleApp = AppItem(
        id = "test_app_1",
        name = "RuStore Test",
        description = "Description",
        category = "Tools",
        developer = "VK",
        ageRating = "12+",
        size = "10 МБ"
    )

    @Before
    fun setUp() {
        getAppsUseCase = GetAppsUseCase(repository)
        getAppDetailUseCase = GetAppDetailUseCase(repository)
    }

    // --- Тесты для GetAppsUseCase ---

    @Test
    fun `GetAppsUseCase should return list of apps from repository`() = runTest {
        // Given
        val expectedList = listOf(sampleApp, sampleApp.copy(id = "test_app_2", name = "App 2"))
        coEvery { repository.getCatalog() } returns expectedList

        // When
        val result = getAppsUseCase()

        // Then
        assertEquals(2, result.size)
        assertEquals(expectedList, result)
        coVerify(exactly = 1) { repository.getCatalog() }
    }

    @Test
    fun `GetAppsUseCase should return empty list when repository returns empty list`() = runTest {
        // Given
        coEvery { repository.getCatalog() } returns emptyList()

        // When
        val result = getAppsUseCase()

        // Then
        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { repository.getCatalog() }
    }

    @Test(expected = RuntimeException::class)
    fun `GetAppsUseCase should rethrow exception when repository fails`() = runTest {
        // Given
        coEvery { repository.getCatalog() } throws RuntimeException("Database error")

        // When
        getAppsUseCase()

        // Then -> RuntimeException thrown
    }

    // --- Тесты для GetAppDetailUseCase ---

    @Test
    fun `GetAppDetailUseCase should return app detail by id from repository`() = runTest {
        // Given
        coEvery { repository.getAppDetail("test_app_1") } returns sampleApp

        // When
        val result = getAppDetailUseCase("test_app_1")

        // Then
        assertEquals("test_app_1", result.id)
        assertEquals("RuStore Test", result.name)
        coVerify(exactly = 1) { repository.getAppDetail("test_app_1") }
    }

    @Test
    fun `GetAppDetailUseCase should invoke repository with exact requested id argument`() = runTest {
        // Given
        val targetId = "target_app_42"
        val expectedApp = sampleApp.copy(id = targetId)
        coEvery { repository.getAppDetail(targetId) } returns expectedApp

        // When
        val result = getAppDetailUseCase(targetId)

        // Then
        assertEquals(targetId, result.id)
        coVerify(exactly = 1) { repository.getAppDetail(targetId) }
    }

    @Test(expected = IllegalStateException::class)
    fun `GetAppDetailUseCase should rethrow exception when repository throws error`() = runTest {
        // Given
        coEvery { repository.getAppDetail("not_found") } throws IllegalStateException("App not found")

        // When
        getAppDetailUseCase("not_found")

        // Then -> IllegalStateException thrown
    }
}