package com.example.learn_androidstudio.data.repository

import com.example.learn_androidstudio.data.api.AppApiService
import com.example.learn_androidstudio.data.mapper.AppItemMapper
import com.example.learn_androidstudio.data.source.MockAppDataSource
import com.example.learn_androidstudio.domain.model.AppItem
import com.example.learn_androidstudio.domain.repository.AppRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random

class AppRepositoryImpl @Inject constructor(
    private val apiService: AppApiService,
    private val mapper: AppItemMapper
) : AppRepository {

    private var cachedCatalog: List<AppItem> = emptyList()
    private val ratingsCache = mutableMapOf<String, Float>()
    private val installedAppIds = mutableSetOf<String>()

    override suspend fun getCatalog(): List<AppItem> {
        return try {
            val dtoList = apiService.getCatalog()
            val items = dtoList.map { mapper.mapDtoToDomain(it) }
            cachedCatalog = items
            items.map { it.copy(rating = ratingsCache[it.id]) }
        } catch (e: Exception) {
            val mockItems = MockAppDataSource.getAppDtos().map { mapper.mapDtoToDomain(it) }
            cachedCatalog = mockItems
            mockItems.map { it.copy(rating = ratingsCache[it.id]) }
        }
    }

    override suspend fun getAppDetail(id: String): AppItem {
        val baseApp = try {
            val dto = apiService.getAppDetail(id)
            mapper.mapDtoToDomain(dto)
        } catch (e: Exception) {
            cachedCatalog.find { it.id == id }
                ?: getCatalog().find { it.id == id }
                ?: MockAppDataSource.getAppDtos().find { it.id == id }?.let { mapper.mapDtoToDomain(it) }
                ?: throw e
        }
        return baseApp.copy(rating = ratingsCache[id] ?: baseApp.rating)
    }

    override suspend fun getRating(id: String): Float {
        delay(1500)
        val rating = Random.nextInt(30, 51) / 10f
        ratingsCache[id] = rating
        return rating
    }

    override suspend fun loadRatingsSequentially(ids: List<String>): Map<String, Float> {
        val result = mutableMapOf<String, Float>()
        for (id in ids) {
            result[id] = getRating(id)
        }
        return result
    }

    override suspend fun loadRatingsInParallel(ids: List<String>): Map<String, Float> = coroutineScope {
        ids.map { id ->
            async {
                id to getRating(id)
            }
        }.awaitAll().toMap().also { ratingsCache.putAll(it) }
    }

    override fun isAppInstalled(id: String): Boolean = installedAppIds.contains(id)

    override fun setAppInstalled(id: String, isInstalled: Boolean) {
        if (isInstalled) {
            installedAppIds.add(id)
        } else {
            installedAppIds.remove(id)
        }
    }
}