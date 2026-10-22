package com.example.learn_androidstudio

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learn_androidstudio.ui.theme.Learn_androidstudioTheme

@Composable
fun AppListScreen(
    apps: List<AppItem> = mockAppList,
    onItemClick: (AppItem) -> Unit = {} // Передаем AppItem
) {
    Scaffold(
        topBar = {
            AppToolbar()
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
            items(apps) { app ->
                AppItemCard(
                    app = app,
                    modifier = Modifier.clickable { onItemClick(app) } // Клик передает app
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppListScreenPreview() {
    Learn_androidstudioTheme {
        AppListScreen(apps = mockAppList)
    }
}