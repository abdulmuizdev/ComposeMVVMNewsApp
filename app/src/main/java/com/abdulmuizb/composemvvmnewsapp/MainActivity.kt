package com.abdulmuizb.composemvvmnewsapp

import android.os.Bundle
import android.view.SurfaceView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.abdulmuizb.composemvvmnewsapp.ui.theme.ComposeMVVMNewsAppTheme
import com.abdulmuizb.composemvvmnewsapp.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.abdulmuizb.composemvvmnewsapp.model.Article
import com.abdulmuizb.composemvvmnewsapp.util.UiState
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeMVVMNewsAppTheme(darkTheme = true) {
                Surface {
                    val viewModel: NewsViewModel = viewModel()
                    NewsScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun NewsScreen(viewModel: NewsViewModel) {
    val articlesState by viewModel.articlesState.collectAsState()

    when (val state = articlesState) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        is UiState.Success -> {

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Text(
                        text = "Latest News",
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(16.dp) // Add some padding for better visual spacing
                    )
                }

                // Use items to display each article in the list
                items(state.data) { article ->
                    NewsItem(article)
                }
            }

        }

        is UiState.Error -> {
            Text(
                text = state.message,
                modifier = Modifier.fillMaxSize(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.error
            )
        }

    }
}

@Composable
fun NewsItem(article: Article) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { },
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(article.urlToImage),
                contentDescription = null,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()

            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = article.title ?: "",
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = article.description ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray
            )

        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val dummyArticle = Article(
        title = "Dummy Title",
        description = "This is a dummy description for testing.",
        urlToImage = "https://via.placeholder.com/150"
    )
    NewsItem(dummyArticle)
}