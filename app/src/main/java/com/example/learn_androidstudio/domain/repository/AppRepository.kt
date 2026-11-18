package com.example.learn_androidstudio.domain.repository

import com.example.learn_androidstudio.domain.model.AppItem

interface AppRepository {
    suspend fun getCatalog(): List<AppItem>
    suspend fun getAppDetail(id: String): AppItem
}