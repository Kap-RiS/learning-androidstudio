package com.example.learn_androidstudio.domain.repository

import com.example.learn_androidstudio.domain.model.AppItem

interface AppRepository {
    suspend fun getCatalog(): List<AppItem>
    suspend fun getAppDetail(id: String): AppItem
    suspend fun getRating(id: String): Float
    suspend fun loadRatingsSequentially(ids: List<String>): Map<String, Float>
    suspend fun loadRatingsInParallel(ids: List<String>): Map<String, Float>

    // Сохранение состояния установки
    fun isAppInstalled(id: String): Boolean
    fun setAppInstalled(id: String, isInstalled: Boolean)
}