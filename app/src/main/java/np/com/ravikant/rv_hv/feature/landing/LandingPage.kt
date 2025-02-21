package np.com.ravikant.rv_hv.feature.landing

import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import np.com.ravikant.rv_hv.ScreenState
import np.com.ravikant.rv_hv.ui.theme.RVHVTheme
import np.com.ravikant.rv_hv.util.DateTimeUtil
import java.net.URL

@Composable
fun LandingPage(navController: NavController) {
    val landingViewModel: LandingViewModel = viewModel()
    val landingState: LandingState by landingViewModel.landingState.collectAsState()


    val lazyListState = rememberLazyListState()
    val isNearBottom = remember {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            val lastVisibleItem = visibleItems.lastOrNull()?.index ?: -1
            val totalItems = layoutInfo.totalItemsCount

            // Ensure that list is not empty and there are enough items
            totalItems > 0 && lastVisibleItem >= totalItems - 5  // Here i load more when 5 items are left
        }
    }

    LaunchedEffect(lazyListState) {
        snapshotFlow { isNearBottom.value }
            .distinctUntilChanged()
            .collect { nearBottom ->
                if (nearBottom && landingState.screenState != ScreenState.ERROR) {
                    landingViewModel.loadMore()
                }
            }
    }


    RVHVTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            when (landingState.screenState) {
                ScreenState.LOADING -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                }

                ScreenState.SUCCESS -> {
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier.padding(innerPadding),
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                    ) {
                        itemsIndexed(landingState.list) { index, item ->
                            CharacterCard(item, index + 1) {
                                val jsonData = Uri.encode(Json.encodeToString(item)) // Encode JSON
                                navController.navigate("detail/$jsonData") // Pass encoded data
                            }
                        }

                        // Show a loading indicator at the bottom while fetching more data
                        if (landingState.isLoadingMore) {
                            Log.d("LazyList", "Showing bottom loader: ${landingState.isLoadingMore}")
                            item {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                                }
                            }
                        }

                    }
                }

                ScreenState.ERROR -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text("Request failed")
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterCard(item: LandingData, rank: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Rank & Score Section
        Column(
            modifier = Modifier
                .width(60.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = rank.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "${item.score}p",
                fontSize = 12.sp,
                color = Color(0xFFFFA000)
            )

            // Fire emoji for hot posts (if score > 300)
            if (item.score > 300) {
                Icon(
                    imageVector = Icons.Default.Whatshot,
                    contentDescription = "Hot",
                    tint = Color(0xFFFFA000),
                    modifier = Modifier.size(16.dp)

                )
            } else {
                Spacer(modifier = Modifier.size(16.dp))
            }
        }

        // Middle Section (Title, Source, and Details)
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, end = 8.dp)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {
                    Row {
//                        KamelImage(
//                            modifier = Modifier.height(30.dp),
//                            resource = asyncPainterResource(data = "https://upload.wikimedia.org/wikipedia/commons/8/88/World_map_blank_without_borders.svg"),
//                            contentScale = ContentScale.Crop,
//                            contentDescription = "Favicon for ${item.url}"
//                        )
//                        Icon(
//                            imageVector = Icons.Filled.Whatshot,
//                            contentDescription = "Comments",
//                            tint = Color.Red,
//                            modifier = Modifier.size(8.dp)
//                        )
                        val url = ensureProtocol(item.url)

                        Text(
                            text = URL(url).host ?: "Invalid URL",
                            style = MaterialTheme.typography.bodySmall,

                            )

                    }
                    Text(
                        text = "${DateTimeUtil.getRelativeTime(item.time)} - ${item.by}",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Comments",
                        tint = Color.Red,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = item.descendants.toString(),
                        fontSize = 12.sp,
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Options",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

        }

    }
}

fun ensureProtocol(url: String): String {
    return if (!url.startsWith("http://") && !url.startsWith("https://")) {
        "https://$url"
    } else {
        url
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
                CharacterCard(
                    LandingData(
                        id = 12345,
                        by = "Ravi",
                        time = 1740001267,
                        type = "story",
                        url = "https://github.com/ValveSoftware/source-sdk-2013/commit/0759e2e8e179d5352d81d0d4aaded72c1704b7a9",
                        title = "Valve releases Team Fortress 2 codesssss which is the best thing",
                        score = 1453,
                        descendants = 222,
                        iconUrl = "https://www.tuhs.org/favicon.ico"
                    ),
                    rank = 1,
                    onClick = {

                    },
                )

            }
        }
    }
}

