package com.example.learn_androidstudio.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learn_androidstudio.R
import com.example.learn_androidstudio.data.repository.AppRepositoryImpl
import com.example.learn_androidstudio.domain.model.AppItem
import com.example.learn_androidstudio.domain.usecase.GetAppsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AppListUiState(
    val apps: List<AppItem> = emptyList(),
    val isLoading: Boolean = false
)

class AppListViewModel(
    private val getAppsUseCase: GetAppsUseCase = GetAppsUseCase(AppRepositoryImpl())
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppListUiState())
    val uiState: StateFlow<AppListUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiText>()
    val eventFlow: SharedFlow<UiText> = _eventFlow.asSharedFlow()

    init {
        loadApps()
    }

    private fun loadApps() {
        // Данные приходят через Clean Architecture: UseCase -> Repository -> Mapper -> DTO
        val apps = getAppsUseCase()
        _uiState.value = AppListUiState(
            apps = apps,
            isLoading = false
        )
    }

    fun onToolbarClick() {
        viewModelScope.launch {
            _eventFlow.emit(UiText.StringResource(R.string.snackbar_toolbar_clicked))
        }
    }
}