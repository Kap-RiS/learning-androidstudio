package com.example.learn_androidstudio.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.learn_androidstudio.data.repository.AppRepositoryImpl
import com.example.learn_androidstudio.domain.repository.AppRepository

object Screen {
    const val APP_LIST = "app_list"
    const val APP_DETAIL = "app_detail"
}

@Composable
fun AppNavigation(
    repository: AppRepository = AppRepositoryImpl()
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.APP_LIST
    ) {
        composable(Screen.APP_LIST) {
            AppListScreen(
                onItemClick = { clickedApp ->
                    navController.navigate("${Screen.APP_DETAIL}/${clickedApp.id}")
                }
            )
        }

        composable(
            route = "${Screen.APP_DETAIL}/{appId}",
            arguments = listOf(navArgument("appId") { type = NavType.IntType })
        ) { backStackEntry ->
            val appId = backStackEntry.arguments?.getInt("appId") ?: 1
            val allApps = repository.getApps()
            val selectedApp = repository.getAppById(appId) ?: allApps.first()

            AppDetailScreen(
                app = selectedApp,
                appList = allApps,
                onBackClick = {
                    navController.popBackStack()
                },
                onShowcaseItemClick = { showcaseApp ->
                    navController.navigate("${Screen.APP_DETAIL}/${showcaseApp.id}")
                },
                onSimilarAppClick = { similarApp ->
                    navController.navigate("${Screen.APP_DETAIL}/${similarApp.id}")
                }
            )
        }
    }
}