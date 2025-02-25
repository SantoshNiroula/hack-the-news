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
import kotlin.math.min

enum class StoryType {
    TOP_STORIES,
    BEST_STORIES,
    NEW_STORIES,
    ASK_HN,
    SHOW_HN,
    JOB_STORIES,
}

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
    val type: StoryType = StoryType.TOP_STORIES,
)

class TopStoriesViewModel : ViewModel() {
    private val _topStoriesID = MutableStateFlow<List<Int>>(emptyList())

    private val _state = MutableStateFlow(TopStoriesState())
    val state: StateFlow<TopStoriesState> = _state.asStateFlow()
    private var _currentPage: Int = 1

    init {
        fetchData()
    }

    fun changeStoryType(type: StoryType) {
        if (type == _state.value.type) return
        _state.value = TopStoriesState(type = type)
        _currentPage = 1
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(status = TopStoriesStateStatus.LOADING)
            try {
                val stories = ApiClient.fetchStories(getTypeString())
                _topStoriesID.value = stories
                if (_topStoriesID.value.isEmpty()) {
                    _state.value = _state.value.copy(status = TopStoriesStateStatus.FAILURE)
                    return@launch
                }

                val requestList = mutableListOf<Deferred<Item>>()
                for (i in 0..min(PAGE_SIZE - 1, _topStoriesID.value.size)) {
                    requestList.add(async(context = Dispatchers.IO) {
                        ApiClient.fetchItem(_topStoriesID.value[i])
                    })
                }

                val items = awaitAll(*requestList.toTypedArray())

                _state.value = _state.value.copy(
                    items = items,
                    status = TopStoriesStateStatus.SUCCESS,
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(status = TopStoriesStateStatus.FAILURE)
            }
        }
    }

    fun fetchMore() {
        viewModelScope.launch {
            val canFetchMore = canLoadMore() &&
                    !(_state.value.status == TopStoriesStateStatus.LOADING ||
                            _state.value.status == TopStoriesStateStatus.LOADING_MORE)

            if (!canFetchMore) return@launch

            _state.value = _state.value.copy(status = TopStoriesStateStatus.LOADING_MORE)

            try {
                val requestList = mutableListOf<Deferred<Item>>()
                val startIndex = _currentPage * PAGE_SIZE
                val endIndex = min(_topStoriesID.value.size - 1, PAGE_SIZE * (_currentPage + 1))

                for (i in startIndex..endIndex) {
                    requestList.add(async(context = Dispatchers.IO) {
                        ApiClient.fetchItem(_topStoriesID.value[i])
                    })
                }
                val items = awaitAll(*requestList.toTypedArray())
                _state.value = _state.value.copy(
                    items = listOf(
                        *_state.value.items.toTypedArray(),
                        *items.toTypedArray(),
                    ),
                    status = TopStoriesStateStatus.SUCCESS,
                )
                _currentPage++
            } catch (e: Exception) {
                _state.value = _state.value.copy(status = TopStoriesStateStatus.LOAD_MORE_FAILURE)
            }
        }
    }

    private fun canLoadMore(): Boolean {
        return _topStoriesID.value.size >= _currentPage * PAGE_SIZE
    }


    companion object {
        private const val PAGE_SIZE = 20
    }

    private fun getTypeString(): String {
        return when (_state.value.type) {
            StoryType.TOP_STORIES -> "topstories"
            StoryType.BEST_STORIES -> "beststories"
            StoryType.NEW_STORIES -> "newstories"
            StoryType.ASK_HN -> "askstories"
            StoryType.SHOW_HN -> "showstories"
            StoryType.JOB_STORIES -> "jobstories"
        }

    }

}