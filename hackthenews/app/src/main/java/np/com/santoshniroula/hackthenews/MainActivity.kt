package np.com.santoshniroula.hackthenews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import np.com.santoshniroula.hackthenews.app_router.AppNavHost
import np.com.santoshniroula.hackthenews.topstories.TopStories
import np.com.santoshniroula.hackthenews.ui.theme.HackTheNewsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HackTheNewsTheme {
                    AppNavHost(navController = rememberNavController())
            }
        }
    }
}