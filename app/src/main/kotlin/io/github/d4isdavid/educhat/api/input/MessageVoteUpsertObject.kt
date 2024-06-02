package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class MessageVoteUpsertObject(
    val positive: Boolean,
)

fun MessageVoteUpsertObject.toJSON(): JSONObject = JSONObject()
    .put("positive", positive)
