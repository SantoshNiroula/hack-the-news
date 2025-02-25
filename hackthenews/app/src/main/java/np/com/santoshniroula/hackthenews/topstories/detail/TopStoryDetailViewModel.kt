package np.com.santoshniroula.hackthenews.topstories.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import np.com.santoshniroula.hackthenews.topstories.detail.data.TopStoryDetailRepositoryImpl
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

sealed class TopStoryDetailEvent {
    data class FetchDetail(val item: Item) : TopStoryDetailEvent()
}

class TopStoryDetailViewModel(
    private val repository: TopStoryDetailRepository = TopStoryDetailRepositoryImpl()
) : ViewModel() {
    private val _state = MutableStateFlow(value = TopStoryDetailState())
    val state: StateFlow<TopStoryDetailState> = _state.asStateFlow()

    fun addEvent(event: TopStoryDetailEvent) {
        when (event) {
            is TopStoryDetailEvent.FetchDetail -> fetchItemDetail(event.item)
        }
    }

    private fun fetchItemDetail(item: Item) {
        viewModelScope.launch(
            context = Dispatchers.IO
        ) {

            _state.update {
                it.copy(
                    rootItem = item,
                    status = TopStoryDetailStateStatus.LOADING,
                )
            }

            try {
                val items = repository.fetchComments(item)
                _state.update {
                    it.copy(
                        items = items,
                        status = TopStoryDetailStateStatus.SUCCESS,
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(status = TopStoryDetailStateStatus.FAILURE) }
            }
        }
    }
}