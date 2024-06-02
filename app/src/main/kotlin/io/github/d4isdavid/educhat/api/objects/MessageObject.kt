package io.github.d4isdavid.educhat.api.objects

import io.github.d4isdavid.educhat.api.utils.getInstant
import io.github.d4isdavid.educhat.api.utils.nullableInstant
import io.github.d4isdavid.educhat.api.utils.nullableInt
import org.json.JSONObject
import java.time.Instant
import kotlin.properties.Delegates

@Suppress("EmptyMethod")
class MessageObject : APIObject() {

    companion object {
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
    var parentId: Int? = null
        private set
    var authorId by Delegates.notNull<Int>()
        private set
    lateinit var votes: MessageVoteCountObject
        private set

    override fun update(obj: JSONObject) {
        id = obj.getInt("id")
        content = obj.getString("content")
        createdAt = obj.getInstant("createdAt")
        editedAt = obj.nullableInstant("editedAt")
        pinned = obj.getBoolean("pinned")
        parentId = obj.nullableInt("parentId")
        authorId = obj.getJSONObject("author").getInt("id")
        votes = MessageVoteCountObject(obj.getJSONObject("votes"))
    }

    fun putReaction(positive: Boolean) {
        votes.addMe(positive)
    }

    fun removeReaction() {
        votes.removeMe()
    }

}
