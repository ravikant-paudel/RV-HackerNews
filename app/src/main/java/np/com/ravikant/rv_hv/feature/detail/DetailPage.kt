package np.com.ravikant.rv_hv.feature.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun DetailPage(navController: NavController, idx : String) {

    Column(verticalArrangement = Arrangement.Center) {
        Text("Detail $idx")
    }
}