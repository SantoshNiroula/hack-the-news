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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import np.com.santoshniroula.hackthenews.topstories.models.Item
import np.com.santoshniroula.hackthenews.ui.theme.HackTheNewsTheme

@Composable
fun CommentItem(
    comment: Item,
    modifier: Modifier = Modifier,
) {

    val paddingValue = comment.level * 16

    val dividerColor = when (comment.level) {
        1 -> MaterialTheme.colorScheme.primary
        2 -> Color.Blue
        3 -> Color.Green
        4 -> Color.Magenta
        5 -> Color.Red
        else -> Color.Yellow

    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = paddingValue.dp)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (comment.level != 0)
            VerticalDivider(
                color = dividerColor,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp)
            )
        if (comment.level != 0)
            Spacer(modifier = Modifier.width(8.dp))
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    comment.by ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    comment.timeText ?: "", style = MaterialTheme.typography.bodySmall,
                    fontSize = 13.sp,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
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
                style = MaterialTheme.typography.bodySmall,
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