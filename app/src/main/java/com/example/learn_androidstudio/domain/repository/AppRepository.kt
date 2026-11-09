package com.example.learn_androidstudio.domain.repository

import com.example.learn_androidstudio.domain.model.AppItem

interface AppRepository {
    suspend fun getApps(): List<AppItem>
    suspend fun getAppById(id: String): AppItem?
}