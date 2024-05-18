package io.github.d4isdavid.educhat.api.objects

import io.github.d4isdavid.educhat.api.utils.nullableInt
import org.json.JSONObject
import kotlin.properties.Delegates

@Suppress("EmptyMethod")
class PostObject : APIObject() {

    companion object {
        fun getKey(obj: JSONObject) = MessageObject.getKey(obj.getJSONObject("message"))
    }

    var messageId by Delegates.notNull<Int>()
        private set
    lateinit var title: String
        private set
    var locked by Delegates.notNull<Boolean>()
        private set
    var question by Delegates.notNull<Boolean>()
        private set
    var answerId: Int? = null
        private set
    var categoryId by Delegates.notNull<Int>()
        private set

    override fun update(obj: JSONObject) {
        messageId = obj.getJSONObject("message").getInt("id")
        title = obj.getString("title")
        locked = obj.getBoolean("locked")
        question = obj.getBoolean("question")
        answerId = obj.nullableInt("answerId")
        categoryId = obj.getInt("categoryId")
    }

}
