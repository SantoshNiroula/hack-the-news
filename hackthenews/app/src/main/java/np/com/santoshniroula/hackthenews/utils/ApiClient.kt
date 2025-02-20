package np.com.santoshniroula.hackthenews.utils

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import np.com.santoshniroula.hackthenews.topstories.models.Item

object ApiClient {
   private val client = HttpClient(CIO) {
        install(ContentNegotiation){
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }

        defaultRequest {
            url("https://hacker-news.firebaseio.com/v0/")
        }
    }

    suspend fun fetchItem(id: Int): Item {
      return  client.get("item/$id.json").body<Item>()
    }

    suspend fun fetchTopStories(): List<Int> {
        return client.get("topstories.json").body<List<Int>>()
    }
}
