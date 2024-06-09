package io.github.d4isdavid.educhat.api.objects

import io.github.d4isdavid.educhat.api.enums.NotificationType
import io.github.d4isdavid.educhat.api.utils.getInstant
import org.json.JSONObject
import java.time.Instant
import kotlin.properties.Delegates

class NotificationObject : APIObject() {

    companion object {
        fun getKey(obj: JSONObject) = obj.getInt("id")
    }

    var id by Delegates.notNull<Int>()
        private set
    lateinit var type: NotificationType
        private set
    lateinit var createdAt: Instant
        private set
    var read by Delegates.notNull<Boolean>()
        private set
    var userId: Int? = null
        private set
    var postId: Int? = null
        private set
    var messageId: Int? = null
        private set

    override fun update(obj: JSONObject) {
        id = obj.getInt("id")
        type = NotificationType.from(obj.getInt("type"))
        createdAt = obj.getInstant("createdAt")
        read = obj.getBoolean("read")
        userId = obj.optJSONObject("user")?.let { UserObject.getKey(it) }
        postId = obj.optJSONObject("post")?.let { PostObject.getKey(it) }
        messageId = obj.optJSONObject("message")?.let { MessageObject.getKey(it) }
    }

}
