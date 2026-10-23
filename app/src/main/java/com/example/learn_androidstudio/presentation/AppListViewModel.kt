package com.example.learn_androidstudio.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learn_androidstudio.R
import com.example.learn_androidstudio.data.mockAppList
import com.example.learn_androidstudio.domain.AppItem
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

class AppListViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AppListUiState())
    val uiState: StateFlow<AppListUiState> = _uiState.asStateFlow()

    // Используем UiText для передачи сообщений интерфейсу
    private val _eventFlow = MutableSharedFlow<UiText>()
    val eventFlow: SharedFlow<UiText> = _eventFlow.asSharedFlow()

    init {
        loadApps()
    }

    private fun loadApps() {
        _uiState.value = AppListUiState(
            apps = mockAppList,
            isLoading = false
        )
    }

    fun onToolbarClick() {
        viewModelScope.launch {
            _eventFlow.emit(UiText.StringResource(R.string.snackbar_toolbar_clicked))
        }
    }
}