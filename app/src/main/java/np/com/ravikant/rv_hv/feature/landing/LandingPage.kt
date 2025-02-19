package np.com.ravikant.rv_hv.feature.landing

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
                        CharacterCard(item)
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
fun CharacterCard(item: LandingData) {
    Column {
//                KamelImage(
//                    modifier = Modifier
//                        .height(200.dp),
//                    resource = asyncPainterResource(data = character.download_url),
//                    contentScale = ContentScale.Crop,
//                    contentDescription = " ${character.author}"
//                )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = item.title,
            textAlign = TextAlign.Center
        )
    }

}

