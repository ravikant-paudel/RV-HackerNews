package np.com.ravikant.rv_hv.feature.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import kotlinx.serialization.json.Json
import np.com.ravikant.rv_hv.feature.landing.LandingData
@Composable
fun DetailPage(navController: NavController, backStackEntry: NavBackStackEntry) {

    val jsonData = backStackEntry.arguments?.getString("data") ?: return
    val landingData = Json.decodeFromString<LandingData>(jsonData)

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Title: ${landingData.title}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = "By: ${landingData.by}", fontSize = 16.sp)
        Text(text = "Score: ${landingData.score} points", fontSize = 14.sp)
    }

}