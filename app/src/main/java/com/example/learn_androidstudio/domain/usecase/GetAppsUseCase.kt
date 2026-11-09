package com.example.learn_androidstudio.domain.usecase

import com.example.learn_androidstudio.domain.model.AppItem
import com.example.learn_androidstudio.domain.repository.AppRepository

class GetAppsUseCase(
    private val repository: AppRepository
) {
    suspend operator fun invoke(): List<AppItem> {
        return repository.getApps()
    }
}