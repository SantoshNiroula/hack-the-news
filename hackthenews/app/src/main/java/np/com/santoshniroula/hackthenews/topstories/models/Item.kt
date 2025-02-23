package np.com.santoshniroula.hackthenews.topstories.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Item(
    val id: Int,
    val deleted: Boolean? = null,
    val type: String? = null,
    val by: String? = null,
    val time: Int? = null,
    val text: String? = null,
    val dead: Boolean? = null,
    val parent: Int? = null,
    val kids: List<Int>? = null,
    val url: String? = null,
    val score: Int? = null,
    val title: String? = null,
    val parts: List<Int>? = null,
    val descendants: Int? = null,
    val timeText: String? = null,
    val level: Int = 0,
) : Parcelable
