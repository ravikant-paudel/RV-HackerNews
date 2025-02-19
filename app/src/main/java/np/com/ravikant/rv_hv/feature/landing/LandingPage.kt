package np.com.ravikant.rv_hv.feature.landing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import np.com.ravikant.rv_hv.ScreenState


@Composable
fun LandingPage(modifier: Modifier) {

    val landingViewModel: LandingViewModel = viewModel<LandingViewModel>()
    val landingState: LandingState by landingViewModel.landingState.collectAsState()

    when (landingState.screenState) {
        ScreenState.LOADING -> {
            // handle loading animation
            print("Loading the screen state")
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }

        ScreenState.SUCCESS -> {

            print("Loading the SUCCESS state")
            LazyColumn(
                modifier = modifier.fillMaxHeight()
            ) {
                items(
                    items = landingState.list,
                    itemContent = { item ->
                        CharacterCard(item, 1)
                    },
                )
            }
        }

        ScreenState.ERROR -> {

            print("Loading the screen state")
            Text("Request failed")
        }
    }

}

@Composable
fun CharacterCard(item: LandingData, rank: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (rank % 2 == 0) Color(0xFFF5F5F5) else Color(0xFFFFE0B2)) // Alternate Background
            .padding(vertical = 8.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rank & Points Section
        Column(
            modifier = Modifier
                .width(50.dp)
                .padding(end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = rank.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "${item.score}p",
                fontSize = 12.sp,
                color = Color.DarkGray
            )
        }

        // Main News Content
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "github.com", // Example: "github.com"
                fontSize = 12.sp,
                color = Color.Gray
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = "${item.time} - ${item.by}",
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
            }
        }

        // Comments & Upvotes Section
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.ChatBubbleOutline,
                    contentDescription = "Comments",
                    tint = Color(0xFFE53935), // Red color like in the image
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = item.descendants.toString(),
                    fontSize = 12.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Whatshot,
                    contentDescription = "Upvotes",
                    tint = Color(0xFFFFA000), // Orange color like in the image
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = item.score.toString(),
                    fontSize = 12.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CardTest() {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        CharacterCard(
            LandingData(
                id = 12345,
                by = "Ravi",
                time = 43094260,
                type = "story",
                url = "https://github.com/ValveSoftware/source-sdk-2013/commit/0759e2e8e179d5352d81d0d4aaded72c1704b7a9",
                title = "Valve releases Team Fortress 2 code",
                score = 1453,

                ),
            rank = 1,
        )
    }
}

