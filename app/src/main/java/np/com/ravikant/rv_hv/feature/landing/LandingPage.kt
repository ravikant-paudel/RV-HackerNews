package np.com.ravikant.rv_hv.feature.landing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.outlined.ChatBubbleOutline
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
import java.net.URL


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
                modifier = modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                itemsIndexed(landingState.list) { index, item ->
                    CharacterCard(item, index + 1)
                }
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
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Rank & Score Section
        Column(
            modifier = Modifier
                .width(60.dp)
                .background(Color(0xFFFFE0B2))
                .padding(vertical = 18.dp),
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
                .padding(start = 8.dp)
        ) {
            Text(
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = URL(item.url).host,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                text = "${item.time} - ${item.by}",
                fontSize = 12.sp,
                color = Color.DarkGray
            )
        }

        // Right Section (Comments & More Options)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                contentDescription = "Comments",
                tint = Color.Red,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = item.descendants.toString(),
                fontSize = 12.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More Options",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
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
                descendants = 222,
            ),
            rank = 1,
        )

    }
}

