package io.github.d4isdavid.educhat.api.objects

import io.github.d4isdavid.educhat.api.utils.nullableInt
import io.github.d4isdavid.educhat.api.utils.nullableString
import org.json.JSONObject
import kotlin.properties.Delegates

@Suppress("EmptyMethod")
class CategoryObject : APIObject() {

    companion object {
        fun getKey(obj: JSONObject) = obj.getInt("id")
    }

    var id by Delegates.notNull<Int>()
        private set
    lateinit var name: String
        private set
    var description: String? = null
        private set
    var pinned by Delegates.notNull<Boolean>()
        private set
    var locked by Delegates.notNull<Boolean>()
        private set
    var parentId: Int? = null
        private set

    override fun update(obj: JSONObject) {
        id = obj.getInt("id")
        name = obj.getString("name")
        description = obj.nullableString("description")
        pinned = obj.getBoolean("pinned")
        locked = obj.getBoolean("locked")
        parentId = obj.nullableInt("parentId")
    }

}
