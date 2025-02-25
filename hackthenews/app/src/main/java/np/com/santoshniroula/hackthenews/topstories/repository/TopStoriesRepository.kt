package np.com.santoshniroula.hackthenews.topstories.repository

import np.com.santoshniroula.hackthenews.topstories.models.Item

interface TopStoriesRepository {
    suspend fun fetchStories(type: String): List<Int>

    suspend fun fetchItem(id: Int): Item

}