package com.example.learn_androidstudio.data.repository

import com.example.learn_androidstudio.data.api.AppApiService
import com.example.learn_androidstudio.data.mapper.AppItemMapper
import com.example.learn_androidstudio.data.source.MockAppDataSource
import com.example.learn_androidstudio.domain.model.AppItem
import com.example.learn_androidstudio.domain.repository.AppRepository
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val apiService: AppApiService,
    private val mapper: AppItemMapper
) : AppRepository {

    override suspend fun getCatalog(): List<AppItem> {
        return try {
            val dtoList = apiService.getCatalog()
            dtoList.map { mapper.mapDtoToDomain(it) }
        } catch (e: Exception) {
            MockAppDataSource.getAppDtos().map { mapper.mapDtoToDomain(it) }
        }
    }

    override suspend fun getAppDetail(id: String): AppItem {
        return try {
            val dto = apiService.getAppDetail(id)
            mapper.mapDtoToDomain(dto)
        } catch (e: Exception) {
            MockAppDataSource.getAppDtos()
                .find { it.id == id }
                ?.let { mapper.mapDtoToDomain(it) }
                ?: throw e
        }
    }
}