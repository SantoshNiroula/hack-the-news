package np.com.santoshniroula.hackthenews.topstories.detail.data

import np.com.santoshniroula.hackthenews.topstories.models.Item
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class Parser {

    fun parseContent(url: String): List<Item> {
        val document = Jsoup.connect(url).get()

        val commentTree = document.select(".comment-tree")[0]
        val comments = commentTree.select("tr.athing")

        val itemsList = mutableListOf<Item?>()

        comments.map { content ->
            itemsList.add(parseItem(content))
        }

       return itemsList.filterNotNull()

    }

    private fun parseItem(content: Element): Item? {

        val id = content.attr("id")
        val comment = content.select(".commtext")
        if (comment.isEmpty()) return null

        val commentText = comment[0]
        val author = content.select("a.hnuser")[0].text()
        val time = content.select(".age")[0].text()
        val level = content.select("td.ind")[0].attr("indent")

        val navElement = content.select("a.clicky")
        val descendants = navElement.attr("n")

        var parentId: Int? = null

        val parentLink = content.select("a:contains(parent)")
        if (parentLink.isNotEmpty()){
            val parentAttr = parentLink.attr("href")
            if (parentAttr.isNotEmpty()){
                parentId = parentAttr.split("#").last().toInt()
            }
        }

        return Item(
            id = id.toInt(),
            type = "comment",
            by = author,
            text = commentText.toString(),
            timeText = time,
            descendants = descendants.toInt(),
            parent = parentId,
            level = level.toInt(),
        )

    }
}