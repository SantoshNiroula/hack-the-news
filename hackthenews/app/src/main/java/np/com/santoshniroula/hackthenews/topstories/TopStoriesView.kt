package np.com.santoshniroula.hackthenews.topstories

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.ktor.http.Url
import np.com.santoshniroula.hackthenews.topstories.models.Item
import np.com.santoshniroula.hackthenews.R
import np.com.santoshniroula.hackthenews.ui.theme.HackTheNewsTheme

@Composable
fun TopStories(modifier: Modifier) {
    val topStoriesViewModel: TopStoriesViewModel = viewModel()
    val topStories = topStoriesViewModel.state.collectAsState()
    val state = topStories.value

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when (state.status) {
            TopStoriesStateStatus.LOADING -> CircularProgressIndicator()
            TopStoriesStateStatus.FAILURE -> Text(text = "Something went wrong")
            else -> ItemListing(
                state.items,
                onFetchMore = {
                    topStoriesViewModel.fetchMore()
                },
                stateStatus = state.status,
            )
        }
    }
}


@Composable
fun ItemListing(
    items: List<Item>,
    onFetchMore: () -> Unit = {},
    stateStatus: TopStoriesStateStatus = TopStoriesStateStatus.SUCCESS
) {

    val lazyListState = rememberLazyListState()

    val shouldLoadMore = remember {
        derivedStateOf {
            (lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -9) >=
                    lazyListState.layoutInfo.totalItemsCount - 6
        }
    }

    LaunchedEffect(key1 = shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            onFetchMore()
        }
    }


    if (items.isEmpty()) {
        Text(text = "No items to show")
        return
    }


    LazyColumn(
        state = lazyListState
    ) {
        items(items) { item ->
            Box(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                ItemCard(item)
            }
        }

        item {
            when (stateStatus) {
                TopStoriesStateStatus.LOADING_MORE -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                TopStoriesStateStatus.LOAD_MORE_FAILURE -> Text(text = "Something went wrong")
                else -> {}
            }
        }
    }

}

@Composable
fun ItemCard(
    item: Item,
    onClick: () -> Unit = {},
    onCommentClick: () -> Unit = {},
) {
    val urlHost = Url(item.url ?: "").host
    val style = MaterialTheme.typography.bodySmall

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // title section
        Surface(
            onClick = onClick,
            modifier = Modifier.weight(5f),
        ) {
            Column(
                modifier = Modifier.padding(start = 16.dp),
            ) {
                Text(
                    text = item.title ?: "",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "${item.score} points",
                        style = style,
                    )
                    DotComponent(
                        modifier = Modifier.padding(2.dp)
                    )
                    Text(
                        urlHost,
                        style = style,
                    )
                }
            }
        }

        Surface(
            onClick = onCommentClick,
            modifier = Modifier.weight(1f)
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(end = 16.dp),
            ) {
                Icon(
                    painterResource(R.drawable.comment),
                    contentDescription = "Comment",
                )
                Text(
                    (item.descendants ?: 0).toString(),
                    style = style,
                )
            }
        }
    }
}

@Composable
private fun DotComponent(
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        Icon(
            painterResource(R.drawable.circle),
            contentDescription = "Dot",
            modifier = Modifier.size(4.dp),
        )
    }
}

@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
annotation class ThemePreviews


@ThemePreviews
@Composable
private fun ItemListingPreview() {
    val items: List<Item> = List(3) {
        Item(
            id = it,
            deleted = false,
            type = "Comment",
            by = "Santosh",
            time = 123456789,
            text = "Hello World",
            dead = false,
            parent = null,
            kids = null,
            url = "https://eater.com/testing",
            score = 127,
            title = "Will the Real Burger King Please Stand Up?",
            parts = null,
            descendants = 127,
        )
    }

    HackTheNewsTheme {
        Surface {
            ItemListing(
                items = items
            )
        }
    }
}

@ThemePreviews
@Composable
private fun ItemCardPreview() {
    HackTheNewsTheme {
        Surface {
            ItemCard(
                item = Item(
                    id = 1,
                    deleted = false,
                    type = "Comment",
                    by = "Santosh",
                    time = 123456789,
                    text = "Hello World",
                    dead = false,
                    parent = null,
                    kids = null,
                    url = "https://eater.com/testing",
                    score = 127,
                    title = "Will the Real Burger King Please Stand Up?",
                    parts = null,
                    descendants = 127,
                )
            )
        }
    }
}