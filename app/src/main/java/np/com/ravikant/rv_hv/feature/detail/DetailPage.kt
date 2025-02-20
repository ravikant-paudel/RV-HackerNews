package np.com.ravikant.rv_hv.feature.detail

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import kotlinx.serialization.json.Json
import np.com.ravikant.rv_hv.feature.landing.LandingData
import np.com.ravikant.rv_hv.ui.theme.RVHVTheme
import np.com.ravikant.rv_hv.util.DateTimeUtil
import java.net.URL

@Composable
fun DetailPage(navController: NavController, backStackEntry: NavBackStackEntry) {
    val jsonData = backStackEntry.arguments?.getString("data") ?: return
    val landingData = Json.decodeFromString<LandingData>(Uri.decode(jsonData))

    Scaffold { innerPadding ->
        DetailChatSection(innerPadding, landingData)
    }
}

@Composable
private fun DetailChatSection(
    innerPadding: PaddingValues,
    landingData: LandingData
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = landingData.title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Row {
//            KamelImage(
//                { asyncPainterResource(data = "https://www.tuhs.org/favicon.ico") },
//                contentDescription = "Favicon for ${landingData.url}",
//                modifier = Modifier.height(60.dp),
//                contentScale = ContentScale.FillHeight,
//            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = URL(landingData.url).host,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Icon(
                imageVector = Icons.Default.ThumbUp,
                contentDescription = "thumb",
                modifier = Modifier.size(16.dp)

            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = landingData.score.toString(),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Message,
                contentDescription = "Message",
                modifier = Modifier.size(16.dp)

            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = landingData.descendants.toString(),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = "Schedule",
                modifier = Modifier.size(16.dp)

            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = DateTimeUtil.getRelativeTime(landingData.time),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "User",
                modifier = Modifier.size(16.dp)

            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = landingData.by,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
        )

    }
}


@Preview(
    name = "DARK",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    name = "LIGHT",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun CardPreview() {

    RVHVTheme {
        Surface {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
            ) {
                DetailChatSection(
                    landingData = LandingData(
                        id = 12345,
                        by = "Ravi",
                        time = 1740001267,
                        type = "story",
                        url = "https://github.com/ValveSoftware/source-sdk-2013/commit/0759e2e8e179d5352d81d0d4aaded72c1704b7a9",
                        title = "1972 Unix V2 \"Beta\" Resurrected",
                        score = 1453,
                        descendants = 222,
                        iconUrl = "https://www.tuhs.org/favicon.ico"
                    ),
                    innerPadding = PaddingValues(all = 16.dp),
                )

            }
        }
    }
}


//@Preview(
//    name = "DARK",
//    showBackground = true,
//    showSystemUi = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES
//)
//@Preview(
//    name = "LIGHT",
//    showBackground = true,
//    showSystemUi = true,
//    uiMode = Configuration.UI_MODE_NIGHT_NO
//)
@Composable
fun TypographyPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Display Large", style = MaterialTheme.typography.displayLarge)
        Text("Display Medium", style = MaterialTheme.typography.displayMedium)
        Text("Display Small", style = MaterialTheme.typography.displaySmall)

        Spacer(modifier = Modifier.height(8.dp))

        Text("Headline Large", style = MaterialTheme.typography.headlineLarge)
        Text("Headline Medium", style = MaterialTheme.typography.headlineMedium)
        Text("Headline Small", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(8.dp))

        Text("Title Large", style = MaterialTheme.typography.titleLarge)
        Text("Title Medium", style = MaterialTheme.typography.titleMedium)
        Text("Title Small", style = MaterialTheme.typography.titleSmall)

        Spacer(modifier = Modifier.height(8.dp))

        Text("Body Large", style = MaterialTheme.typography.bodyLarge)
        Text("Body Medium", style = MaterialTheme.typography.bodyMedium)
        Text("Body Small", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(8.dp))

        Text("Label Large", style = MaterialTheme.typography.labelLarge)
        Text("Label Medium", style = MaterialTheme.typography.labelMedium)
        Text("Label Small", style = MaterialTheme.typography.labelSmall)
    }
}
