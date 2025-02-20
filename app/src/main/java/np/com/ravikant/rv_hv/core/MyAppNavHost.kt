package np.com.ravikant.rv_hv.core

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import np.com.ravikant.rv_hv.MainActivity
import np.com.ravikant.rv_hv.feature.detail.DetailPage
import np.com.ravikant.rv_hv.feature.landing.LandingPage

@Composable
fun MyAppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { LandingPage(navController) }
        composable("details/{idx}") { backStackEntry ->
            val idx = backStackEntry.arguments?.getString("idx") ?: "Unknown"
            DetailPage(navController, idx)
        }
    }
}
