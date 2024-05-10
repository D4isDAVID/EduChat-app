package io.github.d4isdavid.educhat.api.objects

import io.github.d4isdavid.educhat.api.utils.getInstant
import io.github.d4isdavid.educhat.api.utils.getJSONObjects
import io.github.d4isdavid.educhat.api.utils.nullableInstant
import org.json.JSONObject
import java.time.Instant
import kotlin.properties.Delegates

class MessageObject() : APIObject() {

    companion object {
        @Suppress("unused")
        fun getKey(obj: JSONObject) = obj.getInt("id")
    }

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
        private set

    constructor(
        content: String,
        createdAt: Instant = Instant.now(),
        editedAt: Instant? = null,
        pinned: Boolean = false,
        hidden: Boolean = false,
        authorId: Int,
        reactions: MutableMap<String, ReactionCountObject> = mutableMapOf(),
    ) : this() {
        this.content = content
        this.createdAt = createdAt
        this.editedAt = editedAt
        this.pinned = pinned
        this.hidden = hidden
        this.authorId = authorId
        this.reactions = reactions
    }

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

    fun putReaction(emoji: String) {
        if (reactions.contains(emoji)) {
            reactions[emoji]!!.addMe()
            return
        }

        reactions[emoji] = ReactionCountObject(
            JSONObject()
                .put("emoji", emoji)
                .put("count", 1)
                .put("me", true)
        )
    }

    fun removeReaction(emoji: String) {
        if (!reactions.contains(emoji)) {
            return
        }

        reactions[emoji]!!.removeMe()
        if (reactions[emoji]!!.count < 1)
            reactions.remove(emoji)
    }

    class ReactionCountObject(obj: JSONObject) {

        var emoji = obj.getString("emoji")
            private set
        var count = obj.getInt("count")
            private set
        var me = obj.getBoolean("me")
            private set

        fun addMe() {
            if (me) {
                return
            }

            count++
            me = true
        }

        fun removeMe() {
            if (!me) {
                return
            }

            count--
            me = false
        }

    }

}
