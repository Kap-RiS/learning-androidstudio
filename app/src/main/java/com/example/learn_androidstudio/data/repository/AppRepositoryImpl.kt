package com.example.learn_androidstudio.data.repository

import com.example.learn_androidstudio.data.mapper.toDomain
import com.example.learn_androidstudio.data.source.MockAppDataSource
import com.example.learn_androidstudio.domain.model.AppItem
import com.example.learn_androidstudio.domain.repository.AppRepository

class AppRepositoryImpl : AppRepository {
    override fun getApps(): List<AppItem> {
        // Получаем DTO из источника и конвертируем в Domain через Mapper
        return MockAppDataSource.getAppDtos().toDomain()
    }

    override fun getAppById(id: Int): AppItem? {
        return getApps().find { it.id == id }
    }
}