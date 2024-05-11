package io.github.d4isdavid.educhat.api.client

import io.github.d4isdavid.educhat.api.input.AdminPostEditObject
import io.github.d4isdavid.educhat.api.input.AdminPostReplyEditObject
import io.github.d4isdavid.educhat.api.input.PostEditObject
import io.github.d4isdavid.educhat.api.input.PostReplyCreateObject
import io.github.d4isdavid.educhat.api.input.PostReplyEditObject
import io.github.d4isdavid.educhat.api.input.toJSON
import io.github.d4isdavid.educhat.api.objects.MessageObject
import io.github.d4isdavid.educhat.api.objects.PostObject
import io.github.d4isdavid.educhat.api.objects.UserObject
import io.github.d4isdavid.educhat.http.request.handlers.handleJsonArray
import io.github.d4isdavid.educhat.http.request.handlers.handleJsonObject
import io.github.d4isdavid.educhat.http.request.writers.writeJsonObject
import io.github.d4isdavid.educhat.api.utils.Routes
import org.json.JSONObject
import kotlin.reflect.KFunction

class PostsAPI(client: APIClient) {

    private val rest = client.rest
    private val messagesCache = client.messages.cache

    val cache = Cache(client)

    fun get(id: Int) = rest.get(Routes.posts(id.toString())) {
        cache.put(handleJsonObject()!!)
    }

    fun edit(id: Int, input: PostEditObject) = rest.patch(
        Routes.posts(id.toString()),
        hook = {
            writeJsonObject(input.toJSON())
        },
    ) {
        handleJsonObject()?.let { cache.put(it) }
    }

    fun edit(id: Int, input: AdminPostEditObject) = rest.patch(
        Routes.posts(id.toString()),
        hook = {
            writeJsonObject(input.toJSON())
        },
    ) {
        handleJsonObject()?.let { cache.put(it) }
    }

    fun delete(id: Int) = rest.delete(Routes.posts(id.toString())) {
        cache.remove(id)
    }

    fun getReplies(id: Int) = rest.get(Routes.postReplies(id.toString())) {
        messagesCache.put(handleJsonArray()!!)
    }

    fun createReply(id: Int, input: PostReplyCreateObject) = rest.get(
        Routes.postReplies(id.toString()),
        hook = {
            writeJsonObject(input.toJSON())
        },
    ) {
        messagesCache.put(handleJsonObject()!!)
    }

    fun getReply(id: Int, replyId: Int) = rest.get(
        Routes.postReplies(id.toString(), replyId.toString()),
    ) {
        messagesCache.put(handleJsonObject()!!)
    }

    fun editReply(id: Int, replyId: Int, input: PostReplyEditObject) = rest.patch(
        Routes.postReplies(id.toString(), replyId.toString()),
        hook = {
            writeJsonObject(input.toJSON())
        },
    ) {
        handleJsonObject()?.let { cache.put(it) }
    }

    fun editReply(id: Int, replyId: Int, input: AdminPostReplyEditObject) = rest.patch(
        Routes.postReplies(id.toString(), replyId.toString()),
        hook = {
            writeJsonObject(input.toJSON())
        },
    ) {
        handleJsonObject()?.let { cache.put(it) }
    }

    fun deleteReply(id: Int, replyId: Int) = rest.delete(
        Routes.postReplies(id.toString(), replyId.toString()),
    ) {
        cache.remove(id)
    }

    class Cache(private var client: APIClient) : APICache<PostObject>(PostObject::class) {

        fun put(
            value: JSONObject,
            getKey: KFunction<Int>? = null,
            constructor: KFunction<PostObject>? = null,
            messageGetKey: KFunction<Int>? = null,
            messageConstructor: KFunction<MessageObject>? = null,
            userGetKey: KFunction<Int>? = null,
            userConstructor: KFunction<UserObject>? = null,
        ): PostObject {
            val message = value.getJSONObject("message")
            client.messages.cache.put(message, messageGetKey, messageConstructor, userGetKey, userConstructor)

            return super.put(value, getKey, constructor)
        }

        override fun put(value: JSONObject, getKey: KFunction<Int>?, constructor: KFunction<PostObject>?): PostObject {
            return put(value, getKey, constructor)
        }

    }

}
