package np.com.santoshniroula.hackthenews.topstories.detail.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import np.com.santoshniroula.hackthenews.topstories.models.Item
import np.com.santoshniroula.hackthenews.ui.theme.HackTheNewsTheme

@Composable
fun CommentItem(
    comment: Item,
    modifier: Modifier = Modifier,
) {

    val paddingValue = comment.level * 32

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = paddingValue.dp)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (comment.level != 0)
            VerticalDivider(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxHeight().width(2.dp)
            )
        if (comment.level != 0)
            Spacer(modifier = Modifier.width(8.dp))
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    comment.by ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(comment.timeText ?: "", style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                AnnotatedString.fromHtml(
                    comment.text ?: "",
                    linkStyles = TextLinkStyles(
                        style = SpanStyle(
                            textDecoration = TextDecoration.Underline,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    )
                ),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(0.dp)
            )
        }

    }
}

@Preview
@Composable
private fun CommentItemPreview() {
    HackTheNewsTheme {
        Surface {
            CommentItem(
                comment = Item(
                    id = 1,
                    level = 1,
                    timeText = "2 hrs",
                    by = "John Doe",
                    text = "<p>Hello World</p>"
                )
            )
        }
    }
}