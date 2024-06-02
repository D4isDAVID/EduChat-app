package io.github.d4isdavid.educhat.api.client

import io.github.d4isdavid.educhat.api.input.MessageVoteUpsertObject
import io.github.d4isdavid.educhat.api.input.toJSON
import io.github.d4isdavid.educhat.api.objects.UserObject
import io.github.d4isdavid.educhat.api.utils.Routes
import io.github.d4isdavid.educhat.http.request.writers.writeJsonObject

@Suppress("unused")
class VotesAPI(private val client: APIClient) {

    private val rest
        get() = client.rest
    private val messages
        get() = client.messages
    private val users
        get() = client.users

    val cache: MutableMap<Int, MutableSet<UserObject>> = mutableMapOf()

    fun delete(messageId: Int) = rest.delete(Routes.votes(messageId.toString())) {
        cache.remove(messageId)

        Unit
    }

    fun delete(messageId: Int, userId: Int) = rest.delete(
        Routes.votes(messageId.toString(), userId.toString()),
    ) {
        ensureCache(messageId)
        cache[messageId]!!.remove(users.cache.get(userId))

        Unit
    }

    fun upsertSelf(messageId: Int, input: MessageVoteUpsertObject) = rest.put(
        Routes.votes(messageId.toString(), "@me"),
        hook = {
            writeJsonObject(input.toJSON())
        },
    ) {
        ensureCache(messageId)
        cache[messageId]!!.add(users.me!!)
        messages.cache.get(messageId)?.putReaction(input.positive)

        Unit
    }

    fun deleteSelf(messageId: Int) = rest.delete(
        Routes.votes(messageId.toString(), "@me"),
    ) {
        ensureCache(messageId)
        cache[messageId]!!.remove(users.me)
        messages.cache.get(messageId)?.removeReaction()

        Unit
    }

    private fun ensureCache(messageId: Int) {
        if (!cache.contains(messageId))
            cache[messageId] = mutableSetOf()
    }

}