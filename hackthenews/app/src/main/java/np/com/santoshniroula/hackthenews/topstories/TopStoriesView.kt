package np.com.santoshniroula.hackthenews.topstories

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.ktor.http.Url
import np.com.santoshniroula.hackthenews.topstories.models.Item
import np.com.santoshniroula.hackthenews.R
import np.com.santoshniroula.hackthenews.ui.theme.HackTheNewsTheme

@Composable
fun TopStoriesPage(navController: NavController) {
    TopStories(
        onTap = {
            navController.navigate(it)
        },
        onCommentClick = {
            navController.navigate(it)
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopStories(
    modifier: Modifier = Modifier,
    onCommentClick: (Item) -> Unit,
    onTap: (Item) -> Unit = {},
) {
    val topStoriesViewModel: TopStoriesViewModel = viewModel()
    val topStories = topStoriesViewModel.state.collectAsState()
    val state = topStories.value





    val expanded = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = getTitle(state.type)) },
                actions = {
                    IconButton(onClick = { expanded.value = !expanded.value }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options")

                        DropdownMenu(
                            expanded = expanded.value,
                            onDismissRequest = { expanded.value = false }
                        ) {

                            StoryType.entries.map { type ->
                                DropdownMenuItem(
                                    text = { Text(getTitle(type)) },
                                    onClick = {
                                        expanded.value = false
                                        topStoriesViewModel.changeStoryType(type)
                                    }
                                )
                            }

                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            when (state.status) {
                TopStoriesStateStatus.LOADING -> CircularProgressIndicator()
                TopStoriesStateStatus.FAILURE -> Text(text = stringResource(R.string.something_went_wrong))
                else -> ItemListing(
                    state.items,
                    onFetchMore = {
                        topStoriesViewModel.fetchMore()
                    },
                    stateStatus = state.status,
                    onCommentClick = onCommentClick,
                    onTap = onTap,
                )
            }
        }
    }
}

@Composable
private fun getTitle(type: StoryType): String{
    return when (type) {
        StoryType.TOP_STORIES -> stringResource(R.string.top_stories)
        StoryType.BEST_STORIES -> stringResource(R.string.best_stories)
        StoryType.NEW_STORIES -> stringResource(R.string.new_stories)
        StoryType.ASK_HN -> stringResource(R.string.ask_hn)
        StoryType.SHOW_HN -> stringResource(R.string.show_hn)
        StoryType.JOB_STORIES -> stringResource(R.string.jobs)
    }
}



@Composable
fun ItemListing(
    items: List<Item>,
    onFetchMore: () -> Unit = {},
    stateStatus: TopStoriesStateStatus = TopStoriesStateStatus.SUCCESS,
    onCommentClick: (Item) -> Unit,
    onTap: (Item) -> Unit
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
        Text(text = stringResource(R.string.no_items_to_show))
        return
    }


    LazyColumn(
        state = lazyListState
    ) {
        items(items) { item ->
            Box(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                ItemCard(
                    item,
                    onClick = { onTap(item) },
                    onCommentClick = { onCommentClick(item) }
                )
            }
        }

        item {
            when (stateStatus) {
                TopStoriesStateStatus.LOADING_MORE -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                TopStoriesStateStatus.LOAD_MORE_FAILURE -> Text(text = stringResource(R.string.something_went_wrong))
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
                    contentDescription = stringResource(R.string.comment),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier
                        .width(16.dp)
                        .height(16.dp)
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
                items = items,
                onCommentClick = {},
                onTap = {},
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