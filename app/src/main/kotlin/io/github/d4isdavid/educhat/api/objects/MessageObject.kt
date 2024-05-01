package io.github.d4isdavid.educhat.api.objects

import io.github.d4isdavid.educhat.api.utils.getInstant
import io.github.d4isdavid.educhat.api.utils.getJSONObjects
import io.github.d4isdavid.educhat.api.utils.nullableInstant
import org.json.JSONObject
import java.time.Instant
import kotlin.properties.Delegates

class MessageObject : APIObject() {

    var id by Delegates.notNull<Int>()
        private set
    lateinit var content: String
        private set
    lateinit var createdAt: Instant
        private set
    var editedAt: Instant? = null
        private set
    var pinned by Delegates.notNull<Boolean>()
        private set
    var hidden by Delegates.notNull<Boolean>()
        private set
    var authorId by Delegates.notNull<Int>()
        private set
    var reactions = mutableMapOf<String, ReactionCountObject>()

    override fun getKey() = id

    override fun update(obj: JSONObject) {
        id = obj.getInt("id")
        content = obj.getString("content")
        createdAt = obj.getInstant("createdAt")
        editedAt = obj.nullableInstant("editedAt")
        pinned = obj.getBoolean("pinned")
        hidden = obj.getBoolean("hidden")
        authorId = obj.getJSONObject("author").getInt("id")

        reactions.clear()
        obj.getJSONArray("reactions").getJSONObjects().forEach {
            val countObj = ReactionCountObject(it)
            reactions[countObj.emoji] = countObj
        }
    }

    class ReactionCountObject(obj: JSONObject) {

        var emoji = obj.getString("emoji")
            private set
        var count = obj.getInt("count")
            private set
        var me = obj.getBoolean("me")
            private set

    }

}
