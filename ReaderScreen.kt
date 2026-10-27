package app.manhwarex.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.io.File

enum class ReadingMode {
    VERTICAL_WEBTOON,
    PAGED_HORIZONTAL
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReaderScreen(
    pages: List<String>,
    chapterTitle: String,
    initialPage: Int = 0,
    onPageChanged: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    var readingMode by remember { mutableStateOf(ReadingMode.VERTICAL_WEBTOON) }
    var showControls by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { showControls = !showControls }
    ) {
        when (readingMode) {
            ReadingMode.VERTICAL_WEBTOON -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(pages) { index, pagePath ->
                        val imageModel = if (pagePath.startsWith("/")) File(pagePath) else pagePath
                        AsyncImage(
                            model = imageModel,
                            contentDescription = "Halaman ${index + 1}",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            ReadingMode.PAGED_HORIZONTAL -> {
                val pagerState = rememberPagerState(initialPage = initialPage) { pages.size }
                
                LaunchedEffect(pagerState.currentPage) {
                    onPageChanged(pagerState.currentPage)
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { pageIndex ->
                    val pagePath = pages[pageIndex]
                    val imageModel = if (pagePath.startsWith("/")) File(pagePath) else pagePath
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = imageModel,
                            contentDescription = "Halaman ${pageIndex + 1}",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }

        if (showControls) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.DarkGray)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onBackClick) {
                        Text("Kembali", color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = chapterTitle,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(Color.DarkGray)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = {
                        readingMode = if (readingMode == ReadingMode.VERTICAL_WEBTOON) {
                            ReadingMode.PAGED_HORIZONTAL
                        } else {
                            ReadingMode.VERTICAL_WEBTOON
                        }
                    }) {
                        Text(if (readingMode == ReadingMode.VERTICAL_WEBTOON) "Mode: Webtoon (Vertikal)" else "Mode: Paged (Horizontal)")
                    }
                }
            }
        }
    }
}
