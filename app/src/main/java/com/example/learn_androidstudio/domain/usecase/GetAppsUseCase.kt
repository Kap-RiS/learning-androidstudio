package com.example.learn_androidstudio.domain.usecase

import com.example.learn_androidstudio.domain.model.AppItem
import com.example.learn_androidstudio.domain.repository.AppRepository
import javax.inject.Inject

class GetAppsUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend operator fun invoke(): List<AppItem> {
        return repository.getCatalog()
    }
}