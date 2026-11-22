package com.example.learn_androidstudio.domain.usecase

import com.example.learn_androidstudio.domain.model.AppItem
import com.example.learn_androidstudio.domain.repository.AppRepository
import javax.inject.Inject

class GetAppDetailUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend operator fun invoke(id: String): AppItem {
        return repository.getAppDetail(id)
    }
}