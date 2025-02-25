package np.com.santoshniroula.hackthenews.topstories.detail.data

import np.com.santoshniroula.hackthenews.topstories.detail.repository.TopStoryDetailRepository
import np.com.santoshniroula.hackthenews.topstories.models.Item

class TopStoryDetailRepositoryImpl(
    private val parser: Parser = Parser()
) : TopStoryDetailRepository {
    override fun fetchComments(item: Item): List<Item> {
        if (item.kids.isNullOrEmpty()) {
            return emptyList()
        }

        return parser.parseContent("https://news.ycombinator.com/item?id=${item.id}")
    }
}