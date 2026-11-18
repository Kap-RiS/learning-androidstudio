package com.example.learn_androidstudio.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learn_androidstudio.domain.model.AppItem
import com.example.learn_androidstudio.domain.usecase.GetAppsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppListUiState(
    val isLoading: Boolean = false,
    val apps: List<AppItem> = emptyList()
)

@HiltViewModel
class AppListViewModel @Inject constructor(
    private val getAppsUseCase: GetAppsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppListUiState(isLoading = true))
    val uiState: StateFlow<AppListUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiText>()
    val eventFlow: SharedFlow<UiText> = _eventFlow.asSharedFlow()

    init {
        loadApps()
    }

    fun loadApps() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val apps = getAppsUseCase()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        apps = apps
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                _eventFlow.emit(UiText.DynamicString(e.message ?: "Ошибка загрузки"))
            }
        }
    }

    fun onToolbarClick() {
        viewModelScope.launch {
            _eventFlow.emit(UiText.DynamicString("Поиск пока в разработке"))
        }
    }
}