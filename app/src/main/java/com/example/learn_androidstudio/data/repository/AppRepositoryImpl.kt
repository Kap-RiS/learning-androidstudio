package com.example.learn_androidstudio.data.repository

import android.util.Log
import com.example.learn_androidstudio.data.api.AppApiService
import com.example.learn_androidstudio.data.api.RetrofitClient
import com.example.learn_androidstudio.data.mapper.toDomain
import com.example.learn_androidstudio.data.source.MockAppDataSource
import com.example.learn_androidstudio.domain.model.AppItem
import com.example.learn_androidstudio.domain.repository.AppRepository

class AppRepositoryImpl(
    private val apiService: AppApiService = RetrofitClient.apiService
) : AppRepository {

    override suspend fun getApps(): List<AppItem> {
        return try {
            val remoteApps = apiService.getCatalog().toDomain()
            if (remoteApps.isNotEmpty()) {
                Log.d("AppRepository", "Успешно получено ${remoteApps.size} приложений с сервера")
                remoteApps
            } else {
                Log.w("AppRepository", "Сервер вернул пустой список, используем мок-данные")
                MockAppDataSource.getAppDtos().toDomain()
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Ошибка сетевого запроса каталога: ${e.message}", e)
            MockAppDataSource.getAppDtos().toDomain()
        }
    }

    override suspend fun getAppById(id: String): AppItem? {
        return try {
            apiService.getAppDetail(id).toDomain()
        } catch (e: Exception) {
            Log.e("AppRepository", "Ошибка сетевого запроса деталей для id=$id: ${e.message}", e)
            getApps().find { it.id == id }
        }
    }
}