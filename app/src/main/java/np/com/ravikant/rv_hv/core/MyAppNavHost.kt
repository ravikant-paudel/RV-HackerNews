package np.com.ravikant.rv_hv.core

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import np.com.ravikant.rv_hv.feature.detail.DetailPage
import np.com.ravikant.rv_hv.feature.landing.LandingPage

@Composable
fun MyAppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            LandingPage(navController)
        }
//        composable("details/{idx}") { backStackEntry ->
//            val idx = backStackEntry.arguments?.getString("idx") ?: "Unknown"
//            DetailPage(navController, idx)
//        }
        composable(
            "detail/{data}",
            arguments = listOf(navArgument("data") { type = NavType.StringType })
        ) { backStackEntry ->
            DetailPage(navController, backStackEntry)
        }
    }
}
