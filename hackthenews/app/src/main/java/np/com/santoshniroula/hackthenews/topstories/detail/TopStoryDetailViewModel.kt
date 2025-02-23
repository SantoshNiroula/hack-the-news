package np.com.santoshniroula.hackthenews.topstories.detail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import np.com.santoshniroula.hackthenews.topstories.detail.repository.TopStoryDetailRepository
import np.com.santoshniroula.hackthenews.topstories.models.Item

enum class TopStoryDetailStateStatus {
    IDLE,
    LOADING,
    SUCCESS,
    FAILURE,
}

data class TopStoryDetailState(
    val status: TopStoryDetailStateStatus = TopStoryDetailStateStatus.IDLE,
    val items: List<Item> = emptyList(),
    val rootItem: Item? = null,
)

class TopStoryDetailViewModel(
    private val repository: TopStoryDetailRepository = TopStoryDetailRepository()
) : ViewModel() {
    private val _state = MutableStateFlow<TopStoryDetailState>(value = TopStoryDetailState())
    val state: StateFlow<TopStoryDetailState> = _state.asStateFlow()

    fun fetchItemDetail(item: Item) {
        viewModelScope.launch(
            context = Dispatchers.IO
        ){

            _state.value = _state.value.copy(
                rootItem = item,
                status = TopStoryDetailStateStatus.LOADING,
            )

            try {
                val items = repository.fetchComments(item)
                _state.value = _state.value.copy(
                    items = items,
                    status = TopStoryDetailStateStatus.SUCCESS,
                )
            } catch (e: Exception) {
                println(e.message)
                _state.value = _state.value.copy(status = TopStoryDetailStateStatus.FAILURE)
            }
        }
    }
}