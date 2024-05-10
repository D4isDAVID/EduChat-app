package io.github.d4isdavid.educhat.api.objects

import io.github.d4isdavid.educhat.api.utils.nullableInt
import io.github.d4isdavid.educhat.api.utils.nullableString
import org.json.JSONObject
import kotlin.properties.Delegates

class CategoryObject() : APIObject() {

    companion object {
        @Suppress("unused")
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

    constructor(
        name: String,
        description: String? = null,
        pinned: Boolean = false,
        locked: Boolean = false,
        parentId: Int? = null
    ) : this() {
        this.name = name
        this.description = description
        this.pinned = pinned
        this.locked = locked
        this.parentId = parentId
    }

    override fun update(obj: JSONObject) {
        id = obj.getInt("id")
        name = obj.getString("name")
        description = obj.nullableString("description")
        pinned = obj.getBoolean("pinned")
        locked = obj.getBoolean("locked")
        parentId = obj.nullableInt("parentId")
    }

}
