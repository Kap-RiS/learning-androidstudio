package com.example.learn_androidstudio.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learn_androidstudio.R
import com.example.learn_androidstudio.domain.model.AppItem
import com.example.learn_androidstudio.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.measureTimeMillis

data class AppListUiState(
    val isLoading: Boolean = false,
    val apps: List<AppItem> = emptyList()
)

@HiltViewModel
class AppListViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppListUiState(isLoading = true))
    val uiState: StateFlow<AppListUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiText>()
    val eventFlow: SharedFlow<UiText> = _eventFlow.asSharedFlow()

    private var refreshJob: Job? = null

    init {
        loadApps()
    }

    fun loadApps() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val apps = repository.getCatalog()
                _uiState.update { it.copy(apps = apps) }
                // Первичный бенчмарк и загрузка рейтингов
                benchmarkAndLoadRatings(apps.map { it.id })
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                _eventFlow.emit(
                    if (!e.message.isNullOrBlank()) UiText.DynamicString(e.message!!)
                    else UiText.StringResource(R.string.error_loading)
                )
            }
        }
    }

    private suspend fun benchmarkAndLoadRatings(ids: List<String>) {
        if (ids.isEmpty()) {
            _uiState.update { it.copy(isLoading = false) }
            return
        }

        // 1. Замер последовательной загрузки (для 10 приложений)
        val benchmarkIds = if (ids.size >= 10) ids.take(10) else ids
        Log.d("CoroutinesBenchmark", "--- Запуск последовательной загрузки для ${benchmarkIds.size} элементов ---")
        val sequentialTime = measureTimeMillis {
            repository.loadRatingsSequentially(benchmarkIds)
        }
        Log.d("CoroutinesBenchmark", "Последовательная загрузка завершена за: $sequentialTime мс")

        // 2. Замер параллельной загрузки
        Log.d("CoroutinesBenchmark", "--- Запуск параллельной загрузки для ${ids.size} элементов ---")
        var parallelRatings: Map<String, Float> = emptyMap()
        val parallelTime = measureTimeMillis {
            parallelRatings = repository.loadRatingsInParallel(ids)
        }
        Log.d("CoroutinesBenchmark", "Параллельная загрузка завершена за: $parallelTime мс")

        // Выставляем полученные рейтинги в UI
        _uiState.update { state ->
            state.copy(
                isLoading = false,
                apps = state.apps.map { app ->
                    app.copy(rating = parallelRatings[app.id])
                }
            )
        }
    }

    /**
     * Реальное обновление каталога по клику на тулбар:
     * - Отменяет предыдущий запрос, если он ещё выполнялся
     * - Показывает индикатор загрузки
     * - Параллельно запрашивает новые рейтинги со случайными значениями
     * - Обновляет список и кэш
     * - Показывает Snackbar пользователю
     */
    fun onToolbarClick() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val apps = repository.getCatalog()
                // Параллельная загрузка новых рейтингов через async/awaitAll
                val newRatings = repository.loadRatingsInParallel(apps.map { it.id })

                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        apps = apps.map { app ->
                            app.copy(rating = newRatings[app.id])
                        }
                    )
                }

                _eventFlow.emit(UiText.StringResource(R.string.snackbar_toolbar_clicked))
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                _eventFlow.emit(
                    if (!e.message.isNullOrBlank()) UiText.DynamicString(e.message!!)
                    else UiText.StringResource(R.string.error_loading)
                )
            }
        }
    }
}