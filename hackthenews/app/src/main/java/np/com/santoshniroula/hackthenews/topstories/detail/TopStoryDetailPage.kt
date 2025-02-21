package np.com.santoshniroula.hackthenews.topstories.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import np.com.santoshniroula.hackthenews.topstories.models.Item

@Composable
fun TopStoryDetailPage(item: Item) {
    TopStoryDetailView(item)
}

@Composable
private fun TopStoryDetailView(item: Item) {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Text(text = item.title ?: "")
        }
    }
}

