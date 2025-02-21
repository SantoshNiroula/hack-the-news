package np.com.santoshniroula.hackthenews.app_router

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import np.com.santoshniroula.hackthenews.topstories.TopStoriesPage
import np.com.santoshniroula.hackthenews.topstories.detail.TopStoryDetailPage
import np.com.santoshniroula.hackthenews.topstories.models.Item
import kotlin.reflect.typeOf

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Home,
    ) {
        composable<Home> {
            TopStoriesPage(navController)
        }
        composable<Item>(
            typeMap = mapOf(typeOf<Item>() to ItemNavType)
        ) { backStackEntry ->
            val item: Item = backStackEntry.toRoute()
            TopStoryDetailPage(item)
        }
    }

}