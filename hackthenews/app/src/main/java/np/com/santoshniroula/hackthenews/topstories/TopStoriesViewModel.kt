package np.com.santoshniroula.hackthenews.topstories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import np.com.santoshniroula.hackthenews.topstories.models.Item
import np.com.santoshniroula.hackthenews.utils.ApiClient


enum class TopStoriesStateStatus {
    IDLE,
    LOADING,
    SUCCESS,
    FAILURE,
    LOADING_MORE,
    LOAD_MORE_FAILURE,
}


data class TopStoriesState(
    val items: List<Item> = emptyList(),
    val status: TopStoriesStateStatus = TopStoriesStateStatus.IDLE,
)

class TopStoriesViewModel : ViewModel() {
    private val _topStoriesID = MutableStateFlow<List<Int>>(emptyList())

    private val _state = MutableStateFlow(TopStoriesState())
    val state: StateFlow<TopStoriesState> = _state.asStateFlow()
    private var _currentPage: Int = 0

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {

            val canFetch = canLoadMore() &&
                    !(_state.value.status == TopStoriesStateStatus.LOADING
                            || _state.value.status == TopStoriesStateStatus.LOADING_MORE)

            if (!canFetch) {
                return@launch
            }

            val isLoadingMore = _currentPage > 0

            _state.value =
                _state.value.copy(
                    status =
                    if (isLoadingMore)
                        TopStoriesStateStatus.LOADING_MORE
                    else
                        TopStoriesStateStatus.LOADING,
                )

            try {

                // fetch top stories only once
                if (!isLoadingMore) {
                    val stories = ApiClient.fetchTopStories()
                    _topStoriesID.value = stories
                    if (_topStoriesID.value.isEmpty()) {
                        _state.value = _state.value.copy(status = TopStoriesStateStatus.FAILURE)
                        return@launch
                    }
                }

                val requestList = mutableListOf<Deferred<Item>>()
                for (i in PAGE_SIZE * _currentPage..PAGE_SIZE * (_currentPage + 1)) {
                    requestList.add(async(context = Dispatchers.IO) {
                        ApiClient.fetchItem(_topStoriesID.value[i])
                    })
                }

                val items = awaitAll<Item>(*requestList.toTypedArray())

                _state.value =
                    _state.value.copy(
                        items = listOf(
                            *_state.value.items.toTypedArray(),
                            *items.toTypedArray(),
                        ),
                        status = TopStoriesStateStatus.SUCCESS,
                    )

                _currentPage++

            } catch (e: Exception) {
                println(e.message)
                _state.value =
                    _state.value.copy(
                        status = if (isLoadingMore)
                            TopStoriesStateStatus.LOAD_MORE_FAILURE
                        else
                            TopStoriesStateStatus.FAILURE,
                    )
            }
        }
    }

    private fun canLoadMore(): Boolean {
        if (_currentPage == 0) return true
        return _topStoriesID.value.size > _currentPage * PAGE_SIZE
    }


    companion object {
        private const val PAGE_SIZE = 20
    }

}