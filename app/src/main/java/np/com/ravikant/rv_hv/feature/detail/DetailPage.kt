package np.com.ravikant.rv_hv.feature.detail

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import kotlinx.serialization.json.Json
import np.com.ravikant.rv_hv.ScreenState
import np.com.ravikant.rv_hv.feature.landing.LandingData
import np.com.ravikant.rv_hv.ui.theme.RVHVTheme
import np.com.ravikant.rv_hv.util.DateTimeUtil
import java.net.URL

@Composable
fun DetailPage(navController: NavController, backStackEntry: NavBackStackEntry) {
    val jsonData = backStackEntry.arguments?.getString("data") ?: return
    val landingData = Json.decodeFromString<LandingData>(Uri.decode(jsonData))

    val detailViewModel: DetailViewModel = viewModel()
    val detailState: DetailState by detailViewModel.detailState.collectAsState()

    LaunchedEffect(Unit) {
        detailViewModel.fetchDetailApiCall(landingData.id)
    }


    Scaffold {
        Column(modifier = Modifier.padding(it)) {
            DetailSection(it, landingData)
            when (detailState.screenState) {
                ScreenState.LOADING -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                }

                ScreenState.SUCCESS -> {
                    LazyColumn {
                        items(detailState.list) { comment ->
                            CommentItem(comment = comment)
                        }
                    }
                }

                ScreenState.ERROR -> {
                    Text(
                        "Request failed",
                        modifier = Modifier.fillMaxSize(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@Composable
fun CommentItem(
    comment: DetailData
) {
    val padding = maxOf(comment.index * 22, 8)
    val dividerColor = when (comment.index) {
        0 -> Color.Red
        1 -> Color.Blue
        2 -> Color(0xFF006400)
        3 -> Color(0xFFA52A2A)
        4 -> Color.Magenta
        5 -> Color(0xFFFFA500)
        6 -> Color.Yellow
        7 -> Color.Gray
        else -> Color.Green
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = padding.dp, top = 2.dp, end = 8.dp, bottom = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
        ) {
            VerticalDivider(thickness = 4.dp, color = dividerColor)
            Column(modifier = Modifier.padding(16.dp)) {
                Row {
                    Text(
                        text = comment.by ?: "",
                        style = MaterialTheme.typography.bodyMedium.copy(color = dividerColor),
                        fontWeight = FontWeight.Bold

                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = " - ${comment.timeString}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )

                }
                Text(
                    text = getAnnotatedString(comment.text ?: ""),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
fun DetailSection(
    innerPadding: PaddingValues,
    landingData: LandingData
) {
    Column {
        DetailHeaderSection(innerPadding, landingData)
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
        )

    }
}

@Composable
private fun DetailHeaderSection(
    innerPadding: PaddingValues,
    landingData: LandingData
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 16.dp),
    ) {
//        Spacer(modifier = Modifier.height(8.dp))
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
    }
}


private fun getAnnotatedString(htmlString: String): AnnotatedString {
    return AnnotatedString.fromHtml(
        htmlString = htmlString,
        linkStyles = TextLinkStyles(
            style = SpanStyle(
                color = Color.Blue,
                textDecoration = TextDecoration.Underline
            )
        )
    )

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
private fun CommentPreview() {
    RVHVTheme {
        Surface {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
            ) {
                CommentItem(
                    comment =
                    DetailData(
                        id = 12345,
                        by = "Ravi",
                        time = 1740001267,
                        text = "This is text",
                        kids = emptyList(),
                        index = 0,
                        replies = emptyList(),
                        timeString = "1 days ago"
                    )
                )

            }


        }
    }
}


