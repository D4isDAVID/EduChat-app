package io.github.d4isdavid.educhat.api.client

import io.github.d4isdavid.educhat.api.objects.UserObject
import io.github.d4isdavid.educhat.api.utils.Routes
import io.github.d4isdavid.educhat.api.utils.getJSONObjects
import io.github.d4isdavid.educhat.http.request.handlers.handleJsonArray

class ReactionsAPI(private val client: APIClient) {

    private val rest
        get() = client.rest
    private val messages
        get() = client.messages
    private val users
        get() = client.users

    val cache: MutableMap<Int, MutableMap<String, MutableSet<UserObject>>> = mutableMapOf()

    fun delete(messageId: Int) = rest.delete(Routes.reactions(messageId.toString())) {
        ensureCache(messageId)
        cache.remove(messageId)

        Unit
    }

    fun get(messageId: Int, emoji: String) = rest.get(
        Routes.reactions(messageId.toString(), emoji),
    ) {
        ensureCache(messageId, emoji)
        handleJsonArray()!!.getJSONObjects().map {
            val u = users.cache.put(it.getJSONObject("user"))
            cache[messageId]!![emoji]!!.add(u)
            u
        }
    }

    fun delete(messageId: Int, emoji: String) = rest.delete(
        Routes.reactions(messageId.toString(), emoji),
    ) {
        ensureCache(messageId, emoji)
        cache[messageId]!!.remove(emoji)

        Unit
    }

    fun delete(messageId: Int, emoji: String, userId: Int) = rest.delete(
        Routes.reactions(messageId.toString(), emoji, userId.toString()),
    ) {
        ensureCache(messageId, emoji)
        cache[messageId]!![emoji]!!.remove(users.cache.get(userId))

        Unit
    }

    fun create(messageId: Int, emoji: String) = rest.put(
        Routes.reactions(messageId.toString(), emoji, "@me"),
    ) {
        ensureCache(messageId, emoji)
        cache[messageId]!![emoji]!!.add(users.me!!)
        messages.cache.get(messageId)?.putReaction(emoji)

        Unit
    }

    fun deleteSelf(messageId: Int, emoji: String) = rest.delete(
        Routes.reactions(messageId.toString(), emoji, "@me"),
    ) {
        ensureCache(messageId, emoji)
        cache[messageId]!![emoji]!!.remove(users.me)
        messages.cache.get(messageId)?.removeReaction(emoji)

        Unit
    }

    private fun ensureCache(messageId: Int, emoji: String? = null) {
        if (!cache.contains(messageId))
            cache[messageId] = mutableMapOf()
        if (emoji != null && !cache[messageId]!!.contains(emoji))
            cache[messageId]!![emoji] = mutableSetOf()
    }

}