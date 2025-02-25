package np.com.santoshniroula.hackthenews.topstories.detail.repository

import np.com.santoshniroula.hackthenews.topstories.models.Item

interface TopStoryDetailRepository{
    fun fetchComments(item: Item): List<Item>
}