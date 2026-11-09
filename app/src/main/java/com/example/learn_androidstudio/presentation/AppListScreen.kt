package com.example.learn_androidstudio.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.learn_androidstudio.domain.model.AppItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppListScreen(
    viewModel: AppListViewModel,
    uiState: AppListUiState,
    onItemClick: (AppItem) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            snackbarHostState.showSnackbar(
                message = event.asString(context.applicationContext)
            )
        }
    }

    Scaffold(
        topBar = {
            AppToolbar(
                onToolbarClick = { viewModel.onToolbarClick() }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                // Показываем индикатор загрузки, пока идет сетевой запрос
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(uiState.apps) { app ->
                        AppItemCard(
                            app = app,
                            onClick = { onItemClick(app) }
                        )
                    }
                }
            }
        }
    }
}