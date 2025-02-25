package np.com.santoshniroula.hackthenews.topstories.data

import np.com.santoshniroula.hackthenews.topstories.models.Item
import np.com.santoshniroula.hackthenews.topstories.repository.TopStoriesRepository
import np.com.santoshniroula.hackthenews.utils.ApiClient

class TopStoriesRepositoryImpl(
    private val apiClient: ApiClient = ApiClient()
): TopStoriesRepository {
    override suspend fun fetchStories(type: String): List<Int> {
        return apiClient.fetchStories(type)

    }

    override suspend fun fetchItem(id: Int): Item {
        return apiClient.fetchItem(id)
    }
}