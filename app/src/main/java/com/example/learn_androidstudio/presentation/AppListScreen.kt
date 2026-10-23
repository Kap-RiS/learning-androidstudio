package com.example.learn_androidstudio.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learn_androidstudio.domain.AppItem
import com.example.learn_androidstudio.presentation.theme.Learn_androidstudioTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppListScreen(
    viewModel: AppListViewModel = viewModel(),
    onItemClick: (AppItem) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { uiText ->
            // Передаем applicationContext — это убирает предупреждение Линта
            val message = uiText.asString(context.applicationContext)
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        topBar = {
            AppToolbar(
                onToolbarClick = { viewModel.onToolbarClick() }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.apps) { app ->
                AppItemCard(
                    app = app,
                    modifier = Modifier.clickable { onItemClick(app) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppListScreenPreview() {
    Learn_androidstudioTheme {
        AppListScreen(onItemClick = {})
    }
}