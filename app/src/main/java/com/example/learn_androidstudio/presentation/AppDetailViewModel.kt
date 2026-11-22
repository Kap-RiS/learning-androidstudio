package com.example.learn_androidstudio.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learn_androidstudio.domain.model.AppItem
import com.example.learn_androidstudio.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface InstallState {
    data object NotInstalled : InstallState
    data class Installing(val progress: Int) : InstallState // 0..100
    data object Installed : InstallState
}

sealed interface AppDetailUiState {
    data object Loading : AppDetailUiState
    data class Content(
        val app: AppItem,
        val allApps: List<AppItem> = emptyList(),
        val installState: InstallState = InstallState.NotInstalled,
        val similarInstallStates: Map<String, InstallState> = emptyMap()
    ) : AppDetailUiState
    data class Error(val message: UiText) : AppDetailUiState
}

@HiltViewModel
class AppDetailViewModel @Inject constructor(
    private val repository: AppRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val appId: String = checkNotNull(savedStateHandle["appId"])

    private val _uiState = MutableStateFlow<AppDetailUiState>(AppDetailUiState.Loading)
    val uiState: StateFlow<AppDetailUiState> = _uiState.asStateFlow()

    private var installJob: Job? = null
    private val similarInstallJobs = mutableMapOf<String, Job>()

    init {
        loadAppDetail()
    }

    fun loadAppDetail() {
        viewModelScope.launch {
            _uiState.update { AppDetailUiState.Loading }
            try {
                val app = repository.getAppDetail(appId)
                val allApps = repository.getCatalog()

                // Считываем сохраненные состояния установки
                val isInstalled = repository.isAppInstalled(appId)
                val similarStates = allApps.associate {
                    it.id to if (repository.isAppInstalled(it.id)) InstallState.Installed else InstallState.NotInstalled
                }

                _uiState.update {
                    AppDetailUiState.Content(
                        app = app,
                        allApps = allApps,
                        installState = if (isInstalled) InstallState.Installed else InstallState.NotInstalled,
                        similarInstallStates = similarStates
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    AppDetailUiState.Error(
                        if (!e.message.isNullOrBlank()) UiText.DynamicString(e.message!!)
                        else UiText.DynamicString("Ошибка загрузки")
                    )
                }
            }
        }
    }

    // Установка главного приложения
    fun onInstallClick() {
        val current = _uiState.value
        if (current !is AppDetailUiState.Content) return

        if (current.installState is InstallState.Installing) {
            installJob?.cancel()
            installJob = null
            repository.setAppInstalled(appId, false)
            _uiState.update { state ->
                if (state is AppDetailUiState.Content) {
                    state.copy(installState = InstallState.NotInstalled)
                } else state
            }
        } else if (current.installState is InstallState.NotInstalled) {
            installJob = viewModelScope.launch {
                try {
                    for (progress in 0..100 step 5) {
                        delay(150)
                        _uiState.update { state ->
                            if (state is AppDetailUiState.Content) {
                                state.copy(installState = InstallState.Installing(progress))
                            } else state
                        }
                    }
                    repository.setAppInstalled(appId, true)
                    _uiState.update { state ->
                        if (state is AppDetailUiState.Content) {
                            state.copy(installState = InstallState.Installed)
                        } else state
                    }
                } catch (e: CancellationException) {
                    repository.setAppInstalled(appId, false)
                    _uiState.update { state ->
                        if (state is AppDetailUiState.Content) {
                            state.copy(installState = InstallState.NotInstalled)
                        } else state
                    }
                    throw e
                }
            }
        }
    }

    // Установка приложения из блока «Вам может понравиться»
    fun onSimilarAppDownloadClick(similarAppId: String) {
        val current = _uiState.value
        if (current !is AppDetailUiState.Content) return

        val currentState = current.similarInstallStates[similarAppId] ?: InstallState.NotInstalled

        if (currentState is InstallState.Installing) {
            similarInstallJobs[similarAppId]?.cancel()
            similarInstallJobs.remove(similarAppId)
            repository.setAppInstalled(similarAppId, false)
            updateSimilarState(similarAppId, InstallState.NotInstalled)
        } else if (currentState is InstallState.NotInstalled) {
            val job = viewModelScope.launch {
                try {
                    for (progress in 0..100 step 10) {
                        delay(150)
                        updateSimilarState(similarAppId, InstallState.Installing(progress))
                    }
                    repository.setAppInstalled(similarAppId, true)
                    updateSimilarState(similarAppId, InstallState.Installed)
                } catch (e: CancellationException) {
                    repository.setAppInstalled(similarAppId, false)
                    updateSimilarState(similarAppId, InstallState.NotInstalled)
                    throw e
                } finally {
                    similarInstallJobs.remove(similarAppId)
                }
            }
            similarInstallJobs[similarAppId] = job
        }
    }

    private fun updateSimilarState(id: String, state: InstallState) {
        _uiState.update { current ->
            if (current is AppDetailUiState.Content) {
                current.copy(
                    similarInstallStates = current.similarInstallStates + (id to state)
                )
            } else current
        }
    }

    override fun onCleared() {
        super.onCleared()
        installJob?.cancel()
        similarInstallJobs.values.forEach { it.cancel() }
    }
}