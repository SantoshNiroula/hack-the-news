package np.com.santoshniroula.hackthenews.app_router

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import np.com.santoshniroula.hackthenews.topstories.models.Item

/**
 * TopStories page route
 */
@Serializable data object Home

/**
 * NavType for `Item` parcelable
 */
val ItemNavType= object : NavType<Item>(isNullableAllowed = false){
    override fun get(
        bundle: Bundle,
        key: String
    ): Item? {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            bundle.getParcelable(key, Item::class.java)
        }else{
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): Item {
       return Json.decodeFromString<Item>(value)
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: Item
    ) {
        bundle.putParcelable(key, value)
    }

}