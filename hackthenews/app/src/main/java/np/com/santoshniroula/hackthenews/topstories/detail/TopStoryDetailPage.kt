package np.com.santoshniroula.hackthenews.topstories.detail

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import np.com.santoshniroula.hackthenews.R
import np.com.santoshniroula.hackthenews.topstories.detail.composables.CommentItem
import np.com.santoshniroula.hackthenews.topstories.models.Item

@Composable
fun TopStoryDetailPage(item: Item, onBackClick: () -> Unit) {
    TopStoryDetailView(item, onBackClick)
}

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopStoryDetailView(item: Item, onBackClick: () -> Unit) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    val detailViewModel: TopStoryDetailViewModel = viewModel()
    val state by detailViewModel.state.collectAsState()

    LaunchedEffect(key1 = "sample") {
        scaffoldState.bottomSheetState.expand()
        detailViewModel.addEvent(TopStoryDetailEvent.FetchDetail(item))
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            painterResource(R.drawable.arrow_back),
                            contentDescription = stringResource(R.string.go_back),
                        )
                    }
                },
                title = {
                    Text(
                        item.title ?: "",
                        maxLines = 2
                    )
                }
            )
        },
        sheetContent = {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                when (state.status) {
                    TopStoryDetailStateStatus.IDLE -> CircularProgressIndicator()
                    TopStoryDetailStateStatus.LOADING -> CircularProgressIndicator()
                    TopStoryDetailStateStatus.FAILURE -> {
                        Text(stringResource(R.string.something_went_wrong))
                    }

                    TopStoryDetailStateStatus.SUCCESS -> {
                        CommentList(state.items)
                    }
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            if (item.url.isNullOrBlank()) {
                return@Box
            }

            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        webViewClient = WebViewClient()

                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                        settings.setSupportZoom(true)
                    }
                },
                update = { webView ->
                    webView.loadUrl(item.url)
                }
            )
        }
    }
}

@Composable
fun CommentList(comments: List<Item>, modifier: Modifier = Modifier) {
    if (comments.isEmpty()) {
        return Text(stringResource(R.string.no_comments))
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(comments) { CommentItem(it) }
    }
}