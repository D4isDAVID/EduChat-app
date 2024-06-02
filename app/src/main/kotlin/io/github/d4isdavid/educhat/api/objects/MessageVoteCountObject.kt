package io.github.d4isdavid.educhat.api.objects

import io.github.d4isdavid.educhat.api.utils.nullableBoolean
import org.json.JSONObject

class MessageVoteCountObject(obj: JSONObject) {

    var count = obj.getInt("count")
        private set
    var me: Boolean? = obj.nullableBoolean("me")
        private set

    fun addMe(positive: Boolean) {
        if (me != null) {
            return
        }

        if (positive) {
            count++
        } else {
            count--
        }

        me = positive
    }

    fun removeMe() {
        if (me == null) {
            return
        }

        if (me == true) {
            count--
        } else {
            count++
        }

        me = null
    }

}