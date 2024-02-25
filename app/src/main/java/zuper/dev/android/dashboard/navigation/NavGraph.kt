package zuper.dev.android.dashboard.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import zuper.dev.android.dashboard.ui.dashboard.DashboardScreen
import zuper.dev.android.dashboard.ui.jobs.JobScreen

@Composable
fun NavigationGraph(
    navController: NavHostController
){
    NavHost(navController = navController, startDestination = Screen.Dashboard.route){
        composable(Screen.Dashboard.route){
            DashboardScreen{
                navController.navigate(Screen.Jobs.route)
            }
        }
        composable(Screen.Jobs.route) {
            JobScreen(navController)
        }
    }
}