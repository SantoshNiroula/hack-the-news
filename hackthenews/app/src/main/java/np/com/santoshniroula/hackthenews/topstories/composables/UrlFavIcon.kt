package np.com.santoshniroula.hackthenews.topstories.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.ktor.http.Url
import np.com.santoshniroula.hackthenews.R

@Composable
fun UrlFavIcon(url: String) {
    val urlHost = Url(url).host
    Row (
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            painterResource(R.drawable.globe),
            modifier = Modifier.size(12.dp),
            contentDescription = "globe",
            tint = MaterialTheme.colorScheme.surfaceTint
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(urlHost, style = MaterialTheme.typography.bodySmall)
    }
}