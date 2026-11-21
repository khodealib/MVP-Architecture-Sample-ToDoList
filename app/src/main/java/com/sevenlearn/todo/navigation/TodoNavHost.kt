package com.sevenlearn.todo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sevenlearn.todo.ui.detail.TaskDetailRoute
import com.sevenlearn.todo.ui.main.TaskListRoute

@Composable
fun TodoNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.TASK_LIST,
        modifier = modifier,
    ) {
        composable(NavRoutes.TASK_LIST) {
            TaskListRoute(
                onAddTask = {
                    navController.navigate(NavRoutes.taskDetail())
                },
                onEditTask = { taskId ->
                    navController.navigate(NavRoutes.taskDetail(taskId))
                },
            )
        }
        composable(
            route = NavRoutes.TASK_DETAIL,
            arguments = listOf(
                navArgument(NavRoutes.TASK_ID_ARG) {
                    type = NavType.LongType
                    defaultValue = NavRoutes.NO_TASK_ID
                },
            ),
        ) {
            TaskDetailRoute(
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
