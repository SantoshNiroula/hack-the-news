package np.com.santoshniroula.hackthenews.topstories.detail.composables

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import np.com.santoshniroula.hackthenews.R
import np.com.santoshniroula.hackthenews.topstories.composables.UrlFavIcon
import np.com.santoshniroula.hackthenews.topstories.models.Item
import np.com.santoshniroula.hackthenews.ui.theme.HackTheNewsTheme

@Composable
fun DetailHeader(item: Item) {
    val localContext = LocalContext.current
    Column {
        Text(item.title ?: "", style = MaterialTheme.typography.titleMedium)
        UrlFavIcon(item.url ?: "")
        VerticalSpacer()
        Row {
            IconText(R.drawable.thumb_up, (item.score ?: 0).toString())
            HorizontalSpacer(16)
            IconText(R.drawable.comment, (item.kids?.size ?: 0).toString())
            HorizontalSpacer(16)
            IconText(R.drawable.time, item.timeText ?: "")
            HorizontalSpacer(16)
            IconText(R.drawable.person_pin, item.by ?: "")
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            CustomIconButton(
                onClick = {/* TODO: implementation*/ },
                icon = R.drawable.person_pin,
            )
            CustomIconButton(
                onClick = {/* TODO: implementation*/ },
                icon = R.drawable.comment,
            )

            CustomIconButton(
                onClick = {/* TODO: implementation*/ },
                icon = R.drawable.thumb_up,
            )
            CustomIconButton(
                onClick = {/* TODO: implementation*/ },
                icon = R.drawable.bookmark,
            )
            CustomIconButton(
                onClick = {
                    val intent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, item.url ?: "")
                        putExtra(Intent.EXTRA_TITLE, item.title ?: "")
                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        type = "text/plain"
                    }
                    localContext.startActivity(Intent.createChooser(intent, null))
                },
                icon = R.drawable.share,
            )
            CustomIconButton(
                onClick = {/* TODO: implementation*/ },
                icon = R.drawable.more_vert
            )

        }
        HorizontalDivider()
        VerticalSpacer()
    }
}

@Composable
private fun IconText(icon: Int, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painterResource(icon),
            contentDescription = "",
            modifier = Modifier.size(14.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
        )
        HorizontalSpacer(4)
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun CustomIconButton(
    onClick: () -> Unit,
    icon: Int,
) {
    IconButton(onClick = onClick) {
        Icon(
            painterResource(icon),
            contentDescription = "",
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
        )
    }
}

@Composable
private fun HorizontalSpacer(value: Int = 8) {
    Spacer(modifier = Modifier.width(value.dp))
}

@Composable
private fun VerticalSpacer(value: Int = 8) {
    Spacer(modifier = Modifier.height(value.dp))
}

@Preview
@Composable
private fun DetailHeaderPreview() {
    HackTheNewsTheme {
        Surface {
            DetailHeader(
                item = Item(
                    id = 1,
                    title = "macOs Tips and Tricks(2022)",
                    by = "pavel",
                    score = 332,
                    timeText = "5 hrs",
                    kids = listOf(1, 2, 3, 4, 5),
                    url = "https://saurabhs.org",
                )
            )
        }
    }
}