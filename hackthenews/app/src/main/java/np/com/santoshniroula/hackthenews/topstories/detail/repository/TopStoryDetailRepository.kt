package np.com.santoshniroula.hackthenews.topstories.detail.repository

import np.com.santoshniroula.hackthenews.topstories.detail.service.Parser
import np.com.santoshniroula.hackthenews.topstories.models.Item

class TopStoryDetailRepository(
    private val parser: Parser = Parser()
) {

    fun fetchComments(item: Item): List<Item> {
        if (item.kids.isNullOrEmpty()) {
            return emptyList()
        }

        return parser.parseContent("https://news.ycombinator.com/item?id=${item.id}")
    }
}