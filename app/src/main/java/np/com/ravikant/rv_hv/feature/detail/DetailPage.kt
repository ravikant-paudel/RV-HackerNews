package np.com.ravikant.rv_hv.feature.detail

import android.net.Uri
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import kotlinx.serialization.json.Json
import np.com.ravikant.rv_hv.ScreenState
import np.com.ravikant.rv_hv.feature.landing.LandingData
import np.com.ravikant.rv_hv.util.DateTimeUtil
import java.net.URL

@Composable
fun DetailPage(navController: NavController, backStackEntry: NavBackStackEntry) {
    val jsonData = backStackEntry.arguments?.getString("data") ?: return
    val landingData = Json.decodeFromString<LandingData>(Uri.decode(jsonData))

    val detailViewModel: DetailViewModel = viewModel()
    val detailState: DetailState by detailViewModel.detailState.collectAsState()

    val lazyListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        detailViewModel.fetchDetailApiCall(landingData.id)
    }

    // Pagination: load next page when scrolling to the bottom
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                if (visibleItems.isNotEmpty() && visibleItems.last().index >= detailState.list.size - 1) {
                    if (detailState.screenState != ScreenState.LOADING) {
                        detailViewModel.loadNextPage()
                    }
                }
            }
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
                    val expandedComments = remember { mutableStateOf(emptyMap<Int, Boolean>()) }
                    LazyColumn(state = lazyListState) {
                        items(detailState.list) { comment ->
                            CommentItem(
                                comment = comment,
                                onLoadReplies = { commentId ->
                                    // Trigger on-demand loading of immediate replies
                                    detailViewModel.loadRepliesForComment(commentId)
                                },
                                expandedComments = expandedComments
                            )
                        }
                    }
                }
                ScreenState.ERROR -> {
                    Text("Request failed", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
                }
            }
        }
    }
}


@Composable
fun CommentItem(comment: DetailData, expandedComments: MutableState<Map<Int, Boolean>>, onLoadReplies: (Int) -> Unit) {
    val isExpanded = expandedComments.value[comment.id] ?: false

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Comment metadata (author and time)
            Text(
                text = "${comment.by} - ${DateTimeUtil.getRelativeTime(comment.time ?: 0)}",
                style = MaterialTheme.typography.bodySmall,
            )

            // Comment text
            Text(
                text = comment.text ?: "",
                style = MaterialTheme.typography.bodyMedium,
            )

            // Show button if there are potential replies (kids exist) or replies are already loaded
            if ((comment.kids?.isNotEmpty() == true) || comment.replies.isNotEmpty()) {
                Button(
                    onClick = {
                        expandedComments.value = expandedComments.value.toMutableMap().apply {
                            this[comment.id] = !(this[comment.id] ?: false)
                        }
                        if (!isExpanded && comment.replies.isEmpty()) {
                            onLoadReplies(comment.id) // Load replies only when expanding for the first time
                        }
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(if (isExpanded) "Hide Replies" else "Show Replies")
                }

                if (isExpanded) {
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        comment.replies.forEach { reply ->
                            CommentItem(reply, expandedComments, onLoadReplies) // Recursively render replies
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CommentList(comments: List<DetailData>, onLoadReplies: (Int) -> Unit) {
    val expandedComments = remember { mutableStateOf(mapOf<Int, Boolean>()) }

    LazyColumn {
        items(comments) { comment ->
            CommentItem(comment, expandedComments, onLoadReplies)
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
