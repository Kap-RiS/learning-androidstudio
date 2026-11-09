package com.example.learn_androidstudio.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavigation(
    viewModel: AppListViewModel = viewModel()
) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "app_list"
    ) {
        composable("app_list") {
            AppListScreen(
                viewModel = viewModel,
                uiState = uiState,
                onItemClick = { app ->
                    navController.navigate("app_detail/${app.id}")
                }
            )
        }

        composable(
            route = "app_detail/{appId}",
            arguments = listOf(
                navArgument("appId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val appId = backStackEntry.arguments?.getString("appId") ?: return@composable
            val selectedApp = uiState.apps.find { it.id == appId }

            if (selectedApp != null) {
                AppDetailScreen(
                    app = selectedApp,
                    appList = uiState.apps,
                    onBackClick = { navController.popBackStack() },
                    onShowcaseItemClick = { app ->
                        navController.navigate("app_detail/${app.id}")
                    },
                    onSimilarAppClick = { app ->
                        navController.navigate("app_detail/${app.id}")
                    }
                )
            }
        }
    }
}